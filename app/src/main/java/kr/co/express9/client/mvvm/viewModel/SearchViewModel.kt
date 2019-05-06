package kr.co.express9.client.mvvm.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kr.co.express9.client.base.BaseViewModel
import kr.co.express9.client.mvvm.model.ProductRepository
import kr.co.express9.client.mvvm.model.data.Category
import kr.co.express9.client.mvvm.model.data.Product
import kr.co.express9.client.mvvm.model.enumData.CategoryEnum
import kr.co.express9.client.mvvm.model.enumData.StatusEnum
import kr.co.express9.client.util.Logger
import org.koin.standalone.inject

class SearchViewModel : BaseViewModel<SearchViewModel.Event>() {

    private val productRepository: ProductRepository by inject()

    enum class Event {

    }

    private val _categoryList = MutableLiveData<ArrayList<Category>>()
    val categoryList: LiveData<ArrayList<Category>>
        get() = _categoryList

    private fun initCategory() {
        CategoryEnum.values().forEach {
            if (_categoryList.value == null) _categoryList.value = ArrayList()
            Logger.d("why? ${it.name}")
            _categoryList.value!!.add(Category(it.key, 0, ArrayList()))
        }
    }

    fun searchProducts(category: String?, name: String?) {
        initCategory()
        productRepository.searchProducts(category, name)
                .subscribe({
                    Logger.d("searchProducts $it")
                    if (it.status == StatusEnum.SUCCESS) setCategory(it.result)
                }, {
                    Logger.d(it.toString())
                })
                .apply { addDisposable(this) }
    }

    private fun setCategory(products: ArrayList<Product>) {
        products.forEach { product ->
            var isETC = true
            _categoryList.value!!.forEachIndexed { i, category ->
                when (category.name) {
                    CategoryEnum.TOTAL.key -> {
                        category.total += 1
                        category.products.add(product)
                    }
                    product.category -> {
                        category.total += 1
                        category.products.add(product)
                        isETC = false
                    }
                }

                _categoryList.value!![i] = category
            }
            if (isETC) {
                val categorySize = CategoryEnum.values().size - 1
                _categoryList.value!![categorySize].total += 1
                _categoryList.value!![categorySize].products.add(product)
            }
            Logger.d("_categoryList.value ${_categoryList.value}")
        }
    }
}