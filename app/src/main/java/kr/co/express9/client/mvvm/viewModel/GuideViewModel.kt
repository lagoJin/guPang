package kr.co.express9.client.mvvm.viewModel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import kr.co.express9.client.base.BaseViewModel
import kr.co.express9.client.mvvm.model.KakaoRepository
import kr.co.express9.client.mvvm.model.UserRepository
import kr.co.express9.client.mvvm.model.data.User
import kr.co.express9.client.util.Logger
import org.koin.standalone.inject

class GuideViewModel : BaseViewModel() {

    private val kakaoRepository: KakaoRepository by inject()
    private val userRepository: UserRepository by inject()

    val searchAddress: MutableLiveData<String> = MutableLiveData() // two way binding

    private val _addressResultSample = MutableLiveData<String>()
    val addressResultSample: LiveData<String>
        get() = _addressResultSample

    private val _progressView = MutableLiveData<Int>().apply { value = View.INVISIBLE }
    val progressView: LiveData<Int>
        get() = _progressView

    private val _event = MutableLiveData<Event>()
    val event: LiveData<Event>
        get() = _event

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

        kakaoRepository.getAddress(searchAddress.value!!)
//            .observeOn(AndroidSchedulers.mainThread())
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