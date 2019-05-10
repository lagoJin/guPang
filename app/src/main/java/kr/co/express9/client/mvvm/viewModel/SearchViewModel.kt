package kr.co.express9.client.mvvm.viewModel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kr.co.express9.client.base.BaseViewModel
import kr.co.express9.client.mvvm.model.ProductRepository
import kr.co.express9.client.mvvm.model.SuggestionRepository
import kr.co.express9.client.mvvm.model.data.Category
import kr.co.express9.client.mvvm.model.data.Product
import kr.co.express9.client.mvvm.model.data.User
import kr.co.express9.client.mvvm.model.enumData.CategoryEnum
import kr.co.express9.client.mvvm.model.enumData.StatusEnum
import kr.co.express9.client.util.Logger
import org.koin.standalone.inject

class SearchViewModel : BaseViewModel<SearchViewModel.Event>() {

    private val productRepository: ProductRepository by inject()
    private val suggestionRepository: SuggestionRepository by inject()

    enum class Event {

    }

    private val _progressView = MutableLiveData<Int>().apply { value = View.INVISIBLE }
    val progressView: LiveData<Int>
        get() = _progressView

    private val _categoryList = MutableLiveData<ArrayList<Category>>()
    val categoryList: LiveData<ArrayList<Category>>
        get() = _categoryList

    private val _isMarts = MutableLiveData<Boolean>()
    val isMarts: LiveData<Boolean>
        get() = _isMarts

    fun searchProducts(category: String?, name: String?) {
        _isMarts.value = User.getFavoriteMarts().size > 0
        if (!_isMarts.value!!) return
        productRepository.searchProducts(category, name)
                .subscribe({
                    if (it.status == StatusEnum.SUCCESS) setCategory(it.result)
                }, {
                    Logger.d(it.toString())
                })
                .apply { addDisposable(this) }
    }

    private fun setCategory(products: ArrayList<Product>) {
        val tempBucket = ArrayList<Category>()
        CategoryEnum.values().forEach {
            tempBucket.add(Category(it.key, 0, ArrayList()))
        }
        products.forEach { product ->
            var isETC = true
            suggestionRepository.putPref(product.name)
            tempBucket.forEachIndexed { i, category ->
                when (category.name) {
                    CategoryEnum.TOTAL.key -> {
                        tempBucket[i].total += 1
                        tempBucket[i].products.add(product)
                    }
                    product.category -> {
                        tempBucket[i].total += 1
                        tempBucket[i].products.add(product)
                        isETC = false
                    }
                }

                tempBucket[i] = category
            }
            if (isETC) {
                val categorySize = CategoryEnum.values().size - 1
                tempBucket[categorySize].total += 1
                tempBucket[categorySize].products.add(product)
            }
            _categoryList.value = tempBucket
        }
    }

    private fun showProgress() {
        _progressView.value = View.VISIBLE
    }

    private fun hideProgress() {
        _progressView.value = View.INVISIBLE
    }
}