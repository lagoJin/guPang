package kr.co.express9.client.mvvm.viewModel

import android.text.Editable
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import kr.co.express9.client.base.BaseViewModel
import kr.co.express9.client.mvvm.model.KakaoRepository
import kr.co.express9.client.mvvm.model.data.Address
import kr.co.express9.client.util.Logger
import kr.co.express9.client.util.extension.anyTostring
import org.koin.standalone.inject

class KakaoAddressViewModel : BaseViewModel<KakaoAddressViewModel.Event>() {

    private val kakaoRepository: KakaoRepository by inject()

    val searchAddress: MutableLiveData<String> = MutableLiveData() // two way binding

    private val _addressResultSample = MutableLiveData<String>()
    val addressResultSample: LiveData<String>
        get() = _addressResultSample

    private val _realAddress = MutableLiveData<Address>()
    val realAdress: LiveData<Address>
        get() = _realAddress

    private val _addressResult = MutableLiveData<ArrayList<String>>()
    val addressResult: LiveData<ArrayList<String>>
        get() = _addressResult

    private val _progressView = MutableLiveData<Int>().apply { value = View.INVISIBLE }
    val progressView: LiveData<Int>
        get() = _progressView

    enum class Event {
        WRITE_SEARCH_ADDRESS,
        NO_ADDRESS,
        NETWORK_ERROR,
        NETWORK_SUCCESS,
        SEARCH_SUCCESS
    }

    init {
        _addressResult.value = ArrayList()
    }

    /**
     * 주소 to 위도경도
     */
    fun getAddress() {
        if (searchAddress.value == null) {
            _event.value = Event.WRITE_SEARCH_ADDRESS
            return
        }

        kakaoRepository.getAddress(searchAddress.value!!)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showProgress() }
                .subscribe({
                    if (it.meta.total_count == 0) _event.value = Event.NO_ADDRESS
                    else _addressResultSample.value = it.documents.toString()
                    hideProgress()
                }, {
                    Logger.e(it.message!!)
                    _event.value = Event.NETWORK_ERROR
                    hideProgress()
                }).apply { addDisposable(this) }
    }

    fun getAddressList(editable: Editable) {
        if (editable.isEmpty()) {
            _event.value = Event.WRITE_SEARCH_ADDRESS
            return
        }

        kakaoRepository.getAddress(editable.toString())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { showProgress() }
                .subscribe({
                    if (it.meta.total_count == 0) {
                        _event.value = Event.NO_ADDRESS
                    } else {
                        _addressResult.value!!.clear()
                        _realAddress.value = it
                        it.documents.forEach { document ->
                            _addressResult.value!!.add(document.address_name)
                        }
                        _event.value = Event.SEARCH_SUCCESS
                    }
                    hideProgress()
                }, {
                    Logger.e(it.message!!)
                    _event.value = Event.NETWORK_ERROR
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