package kr.co.express9.client.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kr.co.express9.client.R
import kr.co.express9.client.databinding.ItemGoodsBinding
import kr.co.express9.client.mvvm.viewModel.CategoryGoodsViewModel

class GoodsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class VH(val b: ItemGoodsBinding) : RecyclerView.ViewHolder(b.root)

    var arrayList = ArrayList<CategoryGoodsViewModel.GoodsDummy>()

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

    override fun getItemCount(): Int = arrayList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as VH
        holder.b.goods = arrayList[position]
    }
}
