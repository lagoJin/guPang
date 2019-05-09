package kr.co.express9.client.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import kr.co.express9.client.R
import kr.co.express9.client.databinding.ItemCartHistoryBinding
import kr.co.express9.client.mvvm.model.data.CartHistory

class CartHistoryAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class VH(val b: ItemCartHistoryBinding) : RecyclerView.ViewHolder(b.root)

    var cartHistory = ArrayList<CartHistory>()

    override fun onCreateViewHolder(parent: ViewGroup, type: Int): RecyclerView.ViewHolder {
        val binding = DataBindingUtil.inflate<ItemCartHistoryBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_cart_history,
            parent,
            false
        )
        return VH(binding)
    }

    override fun getItemCount(): Int = cartHistory.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, i: Int) {
        holder as VH
        holder.b.cartHistory = cartHistory[i]
    }
}