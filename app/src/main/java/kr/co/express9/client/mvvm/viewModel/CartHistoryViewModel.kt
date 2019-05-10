package kr.co.express9.client.mvvm.viewModel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kr.co.express9.client.base.BaseViewModel
import kr.co.express9.client.mvvm.model.CartRepository
import kr.co.express9.client.mvvm.model.data.CartHistory
import kr.co.express9.client.mvvm.model.enumData.StatusEnum
import org.koin.standalone.inject
import java.util.*

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

    private val _progressView = MutableLiveData<Int>().apply { value = View.INVISIBLE }
    val progressView: LiveData<Int>
        get() = _progressView

    private val dateList = ArrayList<String>()
    private val headerIdx = ArrayList<Int>()

    enum class Event {

    }

    fun nextMonth() {
        var year = _historyMonth.value!!.split(".")[0].toInt()
        var month = _historyMonth.value!!.split(".")[1].toInt() + 1

        // 이번 달 보다 큰 경우 요청 못하도록 추가해야함
        if (month == 13) {
            year += 1
            month = 1
        }
        getHistoryByMonth(year, month)
    }

    fun beforeMonth() {
        var year = _historyMonth.value!!.split(".")[0].toInt()
        var month = _historyMonth.value!!.split(".")[1].toInt() - 1
        if (month == 0) {
            year -= 1
            month = 12
        }
        getHistoryByMonth(year, month)
    }

    fun getHistoryByMonth(year: Int, month: Int) {
        val stringMonth = if (month < 10) "0$month" else month.toString()
        _historyMonth.value = "$year.$stringMonth"
        cartRepository.getHistoryByMonth("$year$stringMonth")
                .doOnSubscribe { showProgress() }
                .subscribe({
                    if (it.status == StatusEnum.SUCCESS) {
                        _cartHistory.value = setHeader(it.result)
                        checkIsCartHistory()
                    }
                    hideProgress()
                }, {
                    hideProgress()
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
            groupIdx.forEach { cartHistoryList[it].totalPrice = totalPrice }
        }
        return cartHistoryList
    }

    private fun checkIsCartHistory() {
        _isCartHistory.value = _cartHistory.value!!.size > 0
    }

    private fun showProgress() {
        _progressView.value = View.VISIBLE
    }

    private fun hideProgress() {
        _progressView.value = View.INVISIBLE
    }

}