package kr.co.express9.client.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import kr.co.express9.client.R
import kr.co.express9.client.databinding.ItemCategoryBinding
import kr.co.express9.client.mvvm.viewModel.CategoryGoodsViewModel
import kr.co.express9.client.util.Logger

class CategoryAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var goodsOrderByCategory = ArrayList<ArrayList<CategoryGoodsViewModel.GoodsDummy>>()

    class VH(val b: ItemCategoryBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ItemCategoryBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.item_category,
            parent,
            false
        )
        binding.goodsAdapter = GoodsAdapter()
        return VH(binding)
    }

    override fun getItemCount(): Int = goodsOrderByCategory.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, i: Int) {
        holder as VH
        holder.b.goodsAdapter?.goodsList = goodsOrderByCategory[i]
        holder.b.goodsAdapter?.notifyDataSetChanged()
    }
}