package kr.co.express9.client.mvvm.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.Single
import kr.co.express9.client.base.BaseViewModel
import kr.co.express9.client.mvvm.model.ProductRepository
import kr.co.express9.client.mvvm.model.data.Product
import kr.co.express9.client.mvvm.model.enumData.StatusEnum
import kr.co.express9.client.util.Logger
import org.koin.standalone.inject

class HomeViewModel : BaseViewModel<HomeViewModel.Event>() {

    private val productRepository: ProductRepository by inject()

    enum class Event {
        NO_PRODUCTS
    }

    private val _products = MutableLiveData<ArrayList<Product>>()
    val products: LiveData<ArrayList<Product>>
        get() = _products

    fun getProducts() {
        productRepository.getProducts()
            .subscribe({
                if(it.status == StatusEnum.SUCCESS) {
                    _products.value = it.result
                    if(it.result.size == 0) _event.value = Event.NO_PRODUCTS
                }
            }, {
                Logger.d(it.toString())
            })
            .apply { addDisposable(this) }
    }
}