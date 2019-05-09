package kr.co.express9.client.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import kr.co.express9.client.R
import kr.co.express9.client.databinding.ItemCartBinding
import kr.co.express9.client.mvvm.model.data.CartProduct

class CartAdapter(val onSelect: (index: Int) -> Unit,
                  val onExpand: (index: Int) -> Unit,
                  val onChangeAmount: (index: Int, isPlus: Boolean) -> Unit) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class VH(val b: ItemCartBinding) : RecyclerView.ViewHolder(b.root)

    enum class Payload(name: String) {
        UPDATE_CART_PRODUCT("UPDATE_CART_PRODUCT")
    }

    var cartProducts = ArrayList<CartProduct>()

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): RecyclerView.ViewHolder {
        val binding = DataBindingUtil.inflate<ItemCartBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_cart,
            parent,
            false
        )
        return VH(binding)
    }

    override fun getItemCount(): Int = cartProducts.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, i: Int) {
        holder as VH
        holder.b.cartProduct = cartProducts[i]
        if (cartProducts[i].isHeader) {
            holder.b.vHead.setOnClickListener {
                onExpand(i)
            }
        }

        holder.b.clBody.setOnClickListener { onSelect(i) }
        holder.b.bMinus.setOnClickListener { onChangeAmount(i, false) }
        holder.b.bPlus.setOnClickListener { onChangeAmount(i, true) }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) return super.onBindViewHolder(holder, position, payloads)
        payloads.forEach { payload ->
            if (payload is Payload) {
                holder as VH
                when (payload) {
                    Payload.UPDATE_CART_PRODUCT -> holder.b.cartProduct = cartProducts[position]
                }
            }
        }
    }
}