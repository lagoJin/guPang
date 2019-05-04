package kr.co.express9.client.mvvm.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kr.co.express9.client.base.BaseViewModel
import kr.co.express9.client.mvvm.model.data.GoodsDummy

class GoodsViewModel : BaseViewModel() {

    private val _goods = MutableLiveData<GoodsDummy>()
    val goods: LiveData<GoodsDummy>
        get() = _goods

    private val _itemNum = MutableLiveData<Int>().apply { value = 0 }
    val itemNum:LiveData<Int>
        get() = _itemNum

    fun setGoods(goods: GoodsDummy) {
        _goods.value = goods
    }

    fun plusItem() {
        _itemNum.value = itemNum.value?.plus(1)
    }

    fun minusItem() {
        if (_itemNum.value!! > 0) _itemNum.value = _itemNum.value!!.minus(1)
    }

    fun resetItem() {
        _itemNum.value = 0
    }
}