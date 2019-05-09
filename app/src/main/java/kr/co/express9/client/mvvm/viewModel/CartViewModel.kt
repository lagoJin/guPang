package kr.co.express9.client.mvvm.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kr.co.express9.client.base.BaseViewModel
import kr.co.express9.client.mvvm.model.CartRepository
import kr.co.express9.client.mvvm.model.data.CartProduct
import kr.co.express9.client.mvvm.model.enumData.StatusEnum
import kr.co.express9.client.util.Logger
import org.koin.standalone.inject

class CartViewModel : BaseViewModel<CartViewModel.Event>() {

    private val cartRepository: CartRepository by inject()

    enum class Event {
        SELECTED,
        NOT_SELECTED
    }

    private val _cartProducts = MutableLiveData<ArrayList<CartProduct>>()
    val cartProducts: LiveData<ArrayList<CartProduct>>
        get() = _cartProducts

    private val _totalPrice = MutableLiveData<Int>().apply { value = 0 }
    val totalPrice: LiveData<Int>
        get() = _totalPrice

    private val _totalSalePrice = MutableLiveData<Int>().apply { value = 0 }
    val totalSalePrice: LiveData<Int>
        get() = _totalSalePrice

    private val _totalPayment = MutableLiveData<Int>().apply { value = 0 }
    val totalPayment: LiveData<Int>
        get() = _totalPayment

    private val _cartProductIsEmpty = MutableLiveData<Boolean>().apply { value = true }
    val cartProductIsEmpty: LiveData<Boolean>
        get() = _cartProductIsEmpty

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
        _totalPayment.value = _totalPrice.value!! + _totalSalePrice.value!!
    }

    fun selectGoods(idx: Int, cb: (index: Int) -> Unit) {
        // 상태변경
        val isSelected = !_cartProducts.value!![idx].isSelected
        _cartProducts.value!![idx].isSelected = isSelected

        // 이벤트 전달
        var isSelectedProduct = false
        _cartProducts.value!!.forEach {
            if (it.isSelected) {
                isSelectedProduct = true
                return@forEach
            }
        }
        _event.value = if (isSelectedProduct) Event.SELECTED
        else Event.NOT_SELECTED

        // 금액변경
        val cart = _cartProducts.value!![idx]
        val price = cart.originalUnitPrice * cart.count
        val salePrice = (cart.saleUnitPrice - cart.originalUnitPrice) * cart.count
        calculatePrice(isSelected, price, salePrice)

        cb(idx)
    }

    fun expandGoods(idx: Int, cb: (startIdx: Int, endIdx: Int) -> Unit) {
        val marketIdx = marketList.indexOf(_cartProducts.value!![idx].martSeq)
        val endIdx = if (headerIdx.size == marketIdx + 1) _cartProducts.value!!.size - 1 else headerIdx[marketIdx + 1] - 1

        val toExpanded = !_cartProducts.value!![idx].isExpanded
        for (index in idx..endIdx) {
            _cartProducts.value!![index].isExpanded = toExpanded
        }
        cb(idx, endIdx + 1)
    }

    fun changeAmount(idx: Int, isPlus: Boolean, cb: (idx: Int) -> Unit) {
        val total = _cartProducts.value!![idx].count
        _cartProducts.value!![idx].count = when {
            isPlus -> total + 1
            total > 1 -> total - 1
            else -> 1
        }

        // 해당 상품이 선택되어 있는 경우 금액 변경
        val cart = _cartProducts.value!![idx]
        if (cart.isSelected) {
            val price = cart.saleUnitPrice
            val salePrice = cart.saleUnitPrice - cart.originalUnitPrice
            calculatePrice(isPlus, price, salePrice)
        }
        cb(idx)
    }

    fun deleteCartProduct(cb: (deletedIndex: Int) -> Unit) {
        // 향후 한번에 배열로 삭제할 수 있도록 변경 될 예정
        _cartProducts.value!!.forEachIndexed { idx, cartProduct ->
            cartRepository.deleteCartProduct(cartProduct.productSeq)
                    .subscribe({
                        if(it.status == StatusEnum.SUCCESS) {
                            _cartProducts.value!!.remove(cartProduct)
                            checkCartProductIsEmpty()
                            cb(idx)
                        }
                    }, {
                        Logger.d(it.toString())
                    }).apply { addDisposable(this) }
        }
    }

    fun getCartProducts() {
        cartRepository.getCartProducts()
                .subscribe({
                    if (it.status == StatusEnum.SUCCESS) {
                        marketList.clear()
                        headerIdx.clear()
                        it.result.forEachIndexed { i, product ->
                            product.isExpanded = true // 초기에 다 펼쳐져 있도록 추가 (서버 데이터 파싱시 false)
                            if (product.martSeq !in marketList) {
                                product.isHeader = true
                                marketList.add(product.martSeq)
                                headerIdx.add(i)
                            }
                        }
                        _cartProducts.value = it.result
                        checkCartProductIsEmpty()
                    }
                }, {

                }).apply { addDisposable(this) }
    }

    private fun checkCartProductIsEmpty() {
        _cartProductIsEmpty.value = _cartProducts.value!!.size == 0
    }
}