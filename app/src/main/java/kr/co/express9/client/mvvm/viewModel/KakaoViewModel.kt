package kr.co.express9.client.mvvm.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kakao.auth.ISessionCallback
import com.kakao.auth.Session
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.MeV2ResponseCallback
import com.kakao.usermgmt.response.MeV2Response
import com.kakao.util.exception.KakaoException
import kr.co.express9.client.base.BaseViewModel
import kr.co.express9.client.util.Logger

class KakaoViewModel : BaseViewModel() {

    val me: LiveData<MeV2Response> get() = _me
    val event: LiveData<Event> get() = _event

    private val _me = MutableLiveData<MeV2Response>()
    private val _event = MutableLiveData<Event>()
    private val _session = Session.getCurrentSession()
    private val sessionCallback = object : ISessionCallback {
        override fun onSessionOpened() {
            requestMe()
        }

        override fun onSessionOpenFailed(exception: KakaoException?) {
            Logger.e("kakao onSessionOpenFailed : $exception")
        }
    }

    enum class Event {
        KAKAO_LOGIN_SUCCESS,
        KAKAO_LOGIN_FAILURE
    }

    private fun requestMe() {
        UserManagement.getInstance().me(object : MeV2ResponseCallback() {
            override fun onSuccess(result: MeV2Response?) {
                result?.let {
                    _me.value = result
                    Logger.d("user id : $result")
                    _event.value = Event.KAKAO_LOGIN_SUCCESS
                }
            }

            override fun onSessionClosed(errorResult: ErrorResult?) {
                Logger.e("kakao onSessionClosed : ${errorResult?.errorMessage}")
            }
        })
    }

    fun setSessionCallback() {
        _session.addCallback(sessionCallback)
        _session.checkAndImplicitOpen()
    }

    fun removeSessionCallback() {
        _session.removeCallback(sessionCallback)
    }
}