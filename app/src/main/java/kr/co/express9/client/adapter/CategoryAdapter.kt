package kr.co.express9.client.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import kr.co.express9.client.R
import kr.co.express9.client.databinding.ItemCategoryBinding
import kr.co.express9.client.mvvm.model.data.GoodsDummy

class CategoryAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var goodsOrderByCategory = ArrayList<ArrayList<GoodsDummy>>()

    class VH(val b: ItemCategoryBinding) : RecyclerView.ViewHolder(b.root) {
        init {
            // 뷰가 생성되지 않는 버그로 인해 재활용하지 않도록 수정
            this.setIsRecyclable(false)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding: ItemCategoryBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
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