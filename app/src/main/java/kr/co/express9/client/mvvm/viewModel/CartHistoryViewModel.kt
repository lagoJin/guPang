package kr.co.express9.client.mvvm.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kr.co.express9.client.base.BaseViewModel
import kr.co.express9.client.mvvm.model.CartRepository
import kr.co.express9.client.mvvm.model.data.CartHistory
import kr.co.express9.client.mvvm.model.enumData.StatusEnum
import org.koin.standalone.inject

class CartHistoryViewModel : BaseViewModel<CartHistoryViewModel.Event>() {

    private val cartRepository: CartRepository by inject()

    private val _cartHistory = MutableLiveData<ArrayList<CartHistory>>()
    val cartHistory: LiveData<ArrayList<CartHistory>>
        get() = _cartHistory

    private val _historyMonth = MutableLiveData<String>()
    val historyMonth: LiveData<String>
        get() = _historyMonth

    private val _isCartHistory = MutableLiveData<Boolean>().apply { value = false }
    val isCartHistory: LiveData<Boolean>
        get() = _isCartHistory

    private val dateList = ArrayList<String>()
    private val headerIdx = ArrayList<Int>()

    enum class Event {

    }

    fun nextMonth() {
        getHistoryByMonth()
    }

    fun beforeMonth() {
        getHistoryByMonth()
    }

    fun getHistoryByMonth() {
        cartRepository.getHistoryByMonth("201905")
            .subscribe({
                if(it.status == StatusEnum.SUCCESS) {
                    _cartHistory.value = setHeader(it.result)
                    checkIsCartHistory()
                }
            }, {

            }).apply { addDisposable(this) }
    }

    private fun setHeader(cartHistoryList: ArrayList<CartHistory>): ArrayList<CartHistory> {
        dateList.clear()
        headerIdx.clear()
        var totalPrice = 0
        val groupIdx = ArrayList<Int>()
        cartHistoryList.forEachIndexed { i, cartHistory ->
            cartHistory.itemPrice = cartHistory.saleUnitPrice * cartHistory.count
            if (cartHistory.purchaseYmd !in dateList) {
                groupIdx.clear()
                totalPrice = 0
                cartHistory.isHeader = true
                dateList.add(cartHistory.purchaseYmd)
                headerIdx.add(i)
            }
            groupIdx.add(i)
            totalPrice += cartHistory.itemPrice
            groupIdx.forEach { cartHistoryList[it].totalPrice = totalPrice}
        }
        return cartHistoryList
    }

    private fun checkIsCartHistory() {
        _isCartHistory.value = _cartHistory.value!!.size > 0
    }

}