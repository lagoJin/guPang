package kr.co.express9.client.mvvm.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kr.co.express9.client.base.BaseViewModel

class GoodsViewModel : BaseViewModel() {

    private val _goods = MutableLiveData<CategoryGoodsViewModel.GoodsDummy>()
    val goods: LiveData<CategoryGoodsViewModel.GoodsDummy>
        get() = _goods

    val itemNum = MutableLiveData<Int>().apply { value = 0 }

    fun setGoods(goods: CategoryGoodsViewModel.GoodsDummy) {
        _goods.value = goods
    }

    fun plusItem() {
        itemNum.value = itemNum.value?.plus(1)
    }

    fun minusItem() {
        if (itemNum.value!! > 0) itemNum.value = itemNum.value!!.minus(1)
    }
}