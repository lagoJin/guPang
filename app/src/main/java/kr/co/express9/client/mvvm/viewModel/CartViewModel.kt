package kr.co.express9.client.mvvm.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kr.co.express9.client.base.BaseViewModel
import kr.co.express9.client.mvvm.model.data.CartGoodsDummy

class CartViewModel : BaseViewModel<CartViewModel.Event>() {

    enum class Event {
        SELECTED,
        NOT_SELECTED
    }

    private val _cartGoods = MutableLiveData<ArrayList<CartGoodsDummy>>()
    val cartGoods: LiveData<ArrayList<CartGoodsDummy>>
        get() = _cartGoods

    private val _totalPrice = MutableLiveData<Int>().apply { value = 0 }
    val totalPrice: LiveData<Int>
        get() = _totalPrice

    private val _totalSalePrice = MutableLiveData<Int>().apply { value = 0 }
    val totalSalePrice: LiveData<Int>
        get() = _totalSalePrice

    private val _totalPayment = MutableLiveData<Int>().apply { value = 0 }
    val totalPayment: LiveData<Int>
        get() = _totalPayment

    private var _selectedList = MutableLiveData<ArrayList<Int>>().apply { value = ArrayList() }
    val selectedList: LiveData<ArrayList<Int>>
        get() = _selectedList

    private val marketList = ArrayList<Int>()
    private val headerIdx = ArrayList<Int>()

    private fun calculatePrice(isPlus: Boolean, price: Int, salePrice: Int) {
        if (isPlus) {
            _totalPrice.value = _totalPrice.value!! + price
            _totalSalePrice.value = _totalSalePrice.value!! + salePrice
        } else {
            _totalPrice.value = _totalPrice.value!! - price
            _totalSalePrice.value = _totalSalePrice.value!! - salePrice
        }
        _totalPayment.value = _totalPrice.value!! - _totalSalePrice.value!!
    }

    fun selectGoods(idx: Int, cb: (index: Int) -> Unit) {
        // 상태변경
        val isSelected = !_cartGoods.value!![idx].isSelected
        _cartGoods.value!![idx].isSelected = isSelected

        // 선택목록에 갱신
        if (isSelected) _selectedList.value!!.add(idx)
        else _selectedList.value!!.remove(idx)

        // 이벤트 전달
        _event.value = if (selectedList.value!!.size > 0) Event.SELECTED
        else Event.NOT_SELECTED

        // 금액변경
        val cart = _cartGoods.value!![idx]
        val price = cart.product.saleUnitPrice * cart.total
        val salePrice = (cart.product.saleUnitPrice - cart.product.originalUnitPrice) * cart.total
        calculatePrice(isSelected, price, salePrice)

        cb(idx)
    }

    fun expandGoods(idx: Int, cb: (startIdx: Int, endIdx: Int) -> Unit) {
        _cartGoods.value!![idx].isExpanded

        val marketIdx = marketList.indexOf(_cartGoods.value!![idx].product.martSeq)
        val endIdx = if (headerIdx.size == marketIdx + 1) _cartGoods.value!!.size - 1 else headerIdx[marketIdx + 1] - 1

        val toExpanded = !_cartGoods.value!![idx].isExpanded
        for (index in idx..endIdx) {
            _cartGoods.value!![index].isExpanded = toExpanded
        }
        cb(idx, endIdx + 1)
    }

    fun changeAmount(idx: Int, isPlus: Boolean, cb: (idx: Int) -> Unit) {
        val total = _cartGoods.value!![idx].total
        _cartGoods.value!![idx].total = when {
            isPlus -> total + 1
            total > 1 -> total - 1
            else -> 1
        }

        // 해당 상품이 선택되어 있는 경우 금액 변경
        val cart = _cartGoods.value!![idx]
        if (cart.isSelected) {
            val price = cart.product.saleUnitPrice
            val salePrice = cart.product.saleUnitPrice - cart.product.originalUnitPrice
            calculatePrice(isPlus, price, salePrice)
        }
        cb(idx)
    }

    fun getGoods() {
        val dummyCartGoods = ArrayList<CartGoodsDummy>()

        marketList.clear()
        headerIdx.clear()
        dummyCartGoods.forEachIndexed { i, cartGoods ->
            if (cartGoods.product.martSeq !in marketList) {
                cartGoods.isHeader = true
                marketList.add(cartGoods.product.martSeq)
                headerIdx.add(i)
            }
        }
        _cartGoods.value = dummyCartGoods
    }
}