package kr.co.express9.client.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import kr.co.express9.client.R
import kr.co.express9.client.databinding.ItemCartBinding
import kr.co.express9.client.mvvm.model.data.CartGoodsDummy

class CartAdapter(val onClick:(Int)-> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class VH(val b: ItemCartBinding) : RecyclerView.ViewHolder(b.root)

    var goodsList = ArrayList<CartGoodsDummy>()
    var marketList = ArrayList<String>()
    var headerIdx = ArrayList<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): RecyclerView.ViewHolder {
        val binding = DataBindingUtil.inflate<ItemCartBinding>(
                LayoutInflater.from(parent.context),
                R.layout.item_cart,
                parent,
                false)
        return VH(binding)
    }

    override fun getItemCount(): Int = goodsList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, i: Int) {
        holder as VH
        holder.b.cartGoods = goodsList[i]
        if (goodsList[i].isHeader) {
            holder.b.vHead.setOnClickListener {
                val marketIdx = marketList.indexOf(goodsList[i].goods.market)
                val endIdx = if (headerIdx.size == marketIdx + 1) itemCount - 1 else headerIdx[marketIdx + 1] - 1

                val toExpanded = !goodsList[i].isExpanded
                for (index in i..endIdx) {
                    goodsList[index].isExpanded = toExpanded
                }

                notifyItemRangeChanged(i, endIdx + 1, "clickHeader")
            }
        }

        holder.b.clLayout.setOnClickListener {
            onClick(i)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) return super.onBindViewHolder(holder, position, payloads)
        payloads.forEach { payload ->
            if (payload is String) {
                holder as VH
                when (payload) {
                    "clickHeader" -> holder.b.cartGoods = goodsList[position]
                }
            }
        }
    }
}