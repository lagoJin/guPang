package kr.co.express9.client.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import kr.co.express9.client.R
import kr.co.express9.client.databinding.ItemCategoryBinding

class CategoryAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class VH(val b: ItemCategoryBinding) : RecyclerView.ViewHolder(b.root)

    private val arrayList = ArrayList<CategoryDummy>()
    private val goodsViewModelList = ArrayList<ArrayList<GoodsDummy>>()

    init {
        initDummyList()
    }

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

    override fun getItemCount(): Int = arrayList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, i: Int) {
        holder as VH
        holder.b.goodsAdapter?.arrayList = goodsViewModelList[i]
    }


    data class CategoryDummy(
        var id: Int,
        var name: String,
        var price: Int
    )

    data class GoodsDummy(
        var id: Int,
        var name: String,
        var price: Int
    )


    fun initDummyList() {
        arrayList.add(CategoryDummy(0, "전체", 70))
        arrayList.add(CategoryDummy(1, "과일/채소", 10))
        arrayList.add(CategoryDummy(2, "정육/계란", 10))
        arrayList.add(CategoryDummy(3, "쌀/잡곡", 10))
        goodsViewModelList.add(getGoods(ArrayList(), 0))
        goodsViewModelList.add(getGoods(ArrayList(), 1))
        goodsViewModelList.add(getGoods(ArrayList(), 2))
        goodsViewModelList.add(getGoods(ArrayList(), 3))
        goodsViewModelList.add(getGoods(ArrayList(), 4))
    }
    fun getGoods(dummy: ArrayList<GoodsDummy>, i: Int): ArrayList<GoodsDummy> {
        when(i) {
            0 -> {
                dummy.add(GoodsDummy(0, "계란", 7000))
                dummy.add(GoodsDummy(0, "계란", 7000))
                dummy.add(GoodsDummy(0, "계란", 7000))
                dummy.add(GoodsDummy(0, "계란", 7000))
                dummy.add(GoodsDummy(0, "사과", 7000))
                dummy.add(GoodsDummy(0, "사과", 7000))
                dummy.add(GoodsDummy(0, "사과", 7000))
                dummy.add(GoodsDummy(0, "사과", 7000))
                dummy.add(GoodsDummy(0, "아이스크림", 7000))
                dummy.add(GoodsDummy(0, "아이스크림", 7000))
                dummy.add(GoodsDummy(0, "아이스크림", 7000))
                dummy.add(GoodsDummy(0, "아이스크림", 7000))
            }
            1 -> {
                dummy.add(GoodsDummy(0, "계란", 7000))
                dummy.add(GoodsDummy(0, "계란", 7000))
                dummy.add(GoodsDummy(0, "계란", 7000))
                dummy.add(GoodsDummy(0, "계란", 7000))
            }
            2 -> {
                dummy.add(GoodsDummy(0, "사과", 7000))
                dummy.add(GoodsDummy(0, "사과", 7000))
                dummy.add(GoodsDummy(0, "사과", 7000))
                dummy.add(GoodsDummy(0, "사과", 7000))
            }
            3 -> {
                dummy.add(GoodsDummy(0, "아이스크림", 7000))
                dummy.add(GoodsDummy(0, "아이스크림", 7000))
                dummy.add(GoodsDummy(0, "아이스크림", 7000))
                dummy.add(GoodsDummy(0, "아이스크림", 7000))
            }
        }
        return dummy
    }

}
