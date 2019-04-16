package kr.co.express9.client.mvvm.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.LogoutResponseCallback
import kr.co.express9.client.base.BaseViewModel
import kr.co.express9.client.util.Logger

class LogoutViewModel: BaseViewModel() {

    enum class Event {
        LOGOUT
    }

    private val _event = MutableLiveData<Event>()
    val event: LiveData<Event>
        get() = _event

    fun logout() {
        UserManagement.getInstance().requestLogout(object : LogoutResponseCallback() {
            override fun onCompleteLogout() {
                _event.value = Event.LOGOUT
            }

            override fun onSuccess(result: Long?) {
                _event.value = Event.LOGOUT
            }
        })
    }
}