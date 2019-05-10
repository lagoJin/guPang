package kr.co.express9.client.mvvm.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kr.co.express9.client.base.BaseViewModel
import kr.co.express9.client.mvvm.model.data.Product
import android.R
import kr.co.express9.client.mvvm.model.CartRepository
import kr.co.express9.client.mvvm.model.ProductRepository
import kr.co.express9.client.mvvm.model.enumData.StatusEnum
import kr.co.express9.client.util.Logger
import org.koin.standalone.inject
import pl.kitek.timertextview.TimerTextView


class ProductViewModel : BaseViewModel<ProductViewModel.Event>() {

    private val cartRepository: CartRepository by inject()
    private val productRepository: ProductRepository by inject()

    enum class Event {

    }

    private val _product = MutableLiveData<Product>()
    val product: LiveData<Product>
        get() = _product

    private val _countDown = MutableLiveData<Long>()
    val countDown: LiveData<Long>
        get() = _countDown

    private val _itemNum = MutableLiveData<Int>().apply { value = 0 }
    val itemNum: LiveData<Int>
        get() = _itemNum

    fun setProduct(product: Product) {
        _product.value = product
        // 시간 파싱해야하는데, 임시로 해둠
        val futureTimestamp = System.currentTimeMillis() + 10 * 60 * 60 * 1000
        _countDown.value = futureTimestamp
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

    fun addCartProduct(cb: (isSuccess: Boolean) -> Unit) {
        cartRepository.addCartProduct(_itemNum.value!!, _product.value!!.productSeq)
                .subscribe({
                    cb(it.status == StatusEnum.SUCCESS)
                }, {
                    Logger.d(it.toString())
                }).apply { addDisposable(this) }
    }

    fun getProduct(productSeq: Int, cb: (Product) -> Unit) {
        productRepository.getProduct(productSeq)
            .subscribe({
                if(it.status == StatusEnum.SUCCESS) cb(it.result)
            }, {
                Logger.d(it.toString())
            }).apply { addDisposable(this) }
    }
}