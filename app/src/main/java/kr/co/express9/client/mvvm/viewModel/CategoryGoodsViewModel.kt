package kr.co.express9.client.mvvm.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kr.co.express9.client.base.BaseViewModel

class CategoryGoodsViewModel : BaseViewModel() {


    private val _categoryList = MutableLiveData<ArrayList<CategoryDummy>>()
    val categoryList: LiveData<ArrayList<CategoryDummy>>
        get() = getCategoryNGood()

    private val _goodsOrderByCategory = MutableLiveData<ArrayList<ArrayList<GoodsDummy>>>()
    val goodsOrderByCategory: LiveData<ArrayList<ArrayList<GoodsDummy>>>
        get() = getGoodsOrderByCategory()


    data class CategoryDummy(
        var id: Int,
        var name: String,
        var total: Int
    )

    data class GoodsDummy(
        var id: Int,
        var name: String,
        var price: Int
    )

    private fun getCategoryNGood(): MutableLiveData<ArrayList<CategoryDummy>> {
        val dummyCategoryList = ArrayList<CategoryDummy>()
        dummyCategoryList.add(CategoryDummy(0, "전체", 16))
        dummyCategoryList.add(CategoryDummy(1, "과일/채소", 2))
        dummyCategoryList.add(CategoryDummy(2, "정육/계란", 2))
        dummyCategoryList.add(CategoryDummy(3, "쌀/잡곡", 2))
        dummyCategoryList.add(CategoryDummy(4, "음료/주류", 2))
        dummyCategoryList.add(CategoryDummy(5, "가공식품 (과자, 라면, 냉동)", 2))
        dummyCategoryList.add(CategoryDummy(6, "유제품", 2))
        dummyCategoryList.add(CategoryDummy(7, "생활용품", 2))
        dummyCategoryList.add(CategoryDummy(8, "기타", 2))
        _categoryList.value = dummyCategoryList

        return _categoryList
    }

    private fun getGoodsOrderByCategory(): MutableLiveData<ArrayList<ArrayList<GoodsDummy>>> {
        val dummyGoodsOrderByCategory = ArrayList<ArrayList<GoodsDummy>>()
        var dummyGoods = ArrayList<GoodsDummy>()
        dummyGoods.add(GoodsDummy(0, "사과", 7000))
        dummyGoods.add(GoodsDummy(1, "배", 7000))
        dummyGoods.add(GoodsDummy(2, "계란", 7000))
        dummyGoods.add(GoodsDummy(3, "왕란", 7000))
        dummyGoods.add(GoodsDummy(4, "오곡", 7000))
        dummyGoods.add(GoodsDummy(5, "현미", 7000))
        dummyGoods.add(GoodsDummy(6, "오렌지주스", 7000))
        dummyGoods.add(GoodsDummy(7, "사이다", 7000))
        dummyGoods.add(GoodsDummy(8, "신라면", 7000))
        dummyGoods.add(GoodsDummy(9, "만두", 7000))
        dummyGoods.add(GoodsDummy(10, "아이스크림", 7000))
        dummyGoods.add(GoodsDummy(11, "우유", 7000))
        dummyGoods.add(GoodsDummy(12, "치약", 7000))
        dummyGoods.add(GoodsDummy(13, "화장지", 7000))
        dummyGoods.add(GoodsDummy(14, "강아지", 7000))
        dummyGoods.add(GoodsDummy(15, "고양이", 7000))
        dummyGoodsOrderByCategory.add(dummyGoods)

        dummyGoods = ArrayList()
        dummyGoods.add(GoodsDummy(0, "사과", 7000))
        dummyGoods.add(GoodsDummy(1, "배", 7000))
        dummyGoodsOrderByCategory.add(dummyGoods)

        dummyGoods = ArrayList()
        dummyGoods.add(GoodsDummy(2, "계란", 7000))
        dummyGoods.add(GoodsDummy(3, "왕란", 7000))
        dummyGoodsOrderByCategory.add(dummyGoods)

        dummyGoods = ArrayList()
        dummyGoods.add(GoodsDummy(4, "오곡", 7000))
        dummyGoods.add(GoodsDummy(5, "현미", 7000))
        dummyGoodsOrderByCategory.add(dummyGoods)

        dummyGoods = ArrayList()
        dummyGoods.add(GoodsDummy(6, "오렌지주스", 7000))
        dummyGoods.add(GoodsDummy(7, "사이다", 7000))
        dummyGoodsOrderByCategory.add(dummyGoods)

        dummyGoods = ArrayList()
        dummyGoods.add(GoodsDummy(8, "신라면", 7000))
        dummyGoods.add(GoodsDummy(9, "만두", 7000))
        dummyGoodsOrderByCategory.add(dummyGoods)

        dummyGoods = ArrayList()
        dummyGoods.add(GoodsDummy(10, "아이스크림", 7000))
        dummyGoods.add(GoodsDummy(11, "우유", 7000))
        dummyGoodsOrderByCategory.add(dummyGoods)

        dummyGoods = ArrayList()
        dummyGoods.add(GoodsDummy(12, "치약", 7000))
        dummyGoods.add(GoodsDummy(13, "화장지", 7000))
        dummyGoodsOrderByCategory.add(dummyGoods)

        dummyGoods = ArrayList()
        dummyGoods.add(GoodsDummy(14, "강아지", 7000))
        dummyGoods.add(GoodsDummy(15, "고양이", 7000))
        dummyGoodsOrderByCategory.add(dummyGoods)
        _goodsOrderByCategory.value = dummyGoodsOrderByCategory
        return _goodsOrderByCategory
    }
}