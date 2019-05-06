package kr.co.express9.client.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import kr.co.express9.client.R
import kr.co.express9.client.databinding.ItemGoodsBinding
import kr.co.express9.client.mvvm.model.data.Product
import kr.co.express9.client.mvvm.view.GoodsActivity
import kr.co.express9.client.util.Logger
import kr.co.express9.client.util.extension.launchActivity

class ProductAdapter(var showTitle: Boolean = false) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class VH(val b: ItemGoodsBinding) : RecyclerView.ViewHolder(b.root)

    var productList = ArrayList<Product>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemGoodsBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.item_goods,
            parent,
            false
        )
        return VH(binding)
    }

    override fun getItemCount(): Int = productList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as VH
        holder.b.title.visibility = if (showTitle && position == 1) View.VISIBLE else View.GONE
        holder.b.product = productList[position]
        holder.b.cvLayout.setOnClickListener {
            it.context.launchActivity<GoodsActivity> {
                putExtra("product", productList[position])
            }
        }
    }
}