package kr.co.express9.client.mvvm.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kr.co.express9.client.base.BaseViewModel
import kr.co.express9.client.mvvm.model.data.Product
import android.R
import android.view.View
import kr.co.express9.client.mvvm.model.CartRepository
import kr.co.express9.client.mvvm.model.ProductRepository
import kr.co.express9.client.mvvm.model.enumData.StatusEnum
import kr.co.express9.client.util.Logger
import org.koin.standalone.inject
import pl.kitek.timertextview.TimerTextView
import java.text.SimpleDateFormat


class ProductViewModel : BaseViewModel<ProductViewModel.Event>() {

    private val cartRepository: CartRepository by inject()
    private val productRepository: ProductRepository by inject()

    enum class Event {
        REMAIN_24HOUR
    }

    private val _progressView = MutableLiveData<Int>().apply { value = View.INVISIBLE }
    val progressView: LiveData<Int>
        get() = _progressView

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

        val startDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse(product.startAt)
        val endDate = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").parse(product.endAt)
        val timer = endDate.time - startDate.time

//        _countDown.value = System.currentTimeMillis() + timer
        if(timer < 24 * 3600 * 1000) _countDown.value = System.currentTimeMillis() + timer
        else _event.value = Event.REMAIN_24HOUR

//        _countDown.value = futureTimestamp
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
                .doOnSubscribe { showProgress() }
                .subscribe({
                    cb(it.status == StatusEnum.SUCCESS)
                    hideProgress()
                }, {
                    Logger.d(it.toString())
                    hideProgress()
                }).apply { addDisposable(this) }
    }

    fun getProduct(productSeq: Int, cb: (Product?) -> Unit) {
        productRepository.getProduct(productSeq)
                .doOnSubscribe { showProgress() }
            .subscribe({
                if(it.status == StatusEnum.SUCCESS) cb(it.result)
                hideProgress()
            }, {
                Logger.d(it.toString())
                hideProgress()
            }).apply { addDisposable(this) }
    }

    private fun showProgress() {
        _progressView.value = View.VISIBLE
    }

    private fun hideProgress() {
        _progressView.value = View.INVISIBLE
    }
}