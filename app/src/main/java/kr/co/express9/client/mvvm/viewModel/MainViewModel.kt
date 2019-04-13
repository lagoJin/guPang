package kr.co.express9.client.mvvm.viewModel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kr.co.express9.client.base.BaseViewModel
import kr.co.express9.client.mvvm.model.api.KakaoAPI
import kr.co.express9.client.util.Logger
import kr.co.express9.client.util.extension.networkCommunication

class MainViewModel(private val kakaoApi: KakaoAPI) : BaseViewModel() {

    val searchAddress: MutableLiveData<String> = MutableLiveData() // two way binding
    val addressResultSample: LiveData<String> get() = _addressResultSample
    val progressView: LiveData<Int> get() = _progressView
    val event: LiveData<Event> get() = _event

    private val _addressResultSample = MutableLiveData<String>()
    private val _progressView = MutableLiveData<Int>().apply { value = View.INVISIBLE }
    private val _event = MutableLiveData<Event>()

    enum class Event {
        WRITE_SEARCH_ADDRESS,
        NO_ADDRESS,
        NETWORK_ERROR
    }

    fun getAddress() {
        if (searchAddress.value == null) {
            _event.value = Event.WRITE_SEARCH_ADDRESS
            return
        }

        kakaoApi.getAddress(searchAddress.value!!)
            .networkCommunication()
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

    private fun showProgress() {
        _progressView.value = View.VISIBLE
    }

    private fun hideProgress() {
        _progressView.value = View.INVISIBLE
    }
}