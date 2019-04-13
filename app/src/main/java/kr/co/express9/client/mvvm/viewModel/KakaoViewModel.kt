package kr.co.express9.client.mvvm.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kakao.auth.ISessionCallback
import com.kakao.auth.Session
import com.kakao.network.ErrorResult
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.LogoutResponseCallback
import com.kakao.usermgmt.callback.MeV2ResponseCallback
import com.kakao.usermgmt.response.MeV2Response
import com.kakao.util.exception.KakaoException
import kr.co.express9.client.base.BaseViewModel
import kr.co.express9.client.mvvm.model.data.KakaoUser
import kr.co.express9.client.util.Logger


class KakaoViewModel : BaseViewModel() {

    val me: LiveData<KakaoUser> get() = _me
    val event: LiveData<Event> get() = _event

    private val _me = MutableLiveData<KakaoUser>()
    private val _event = MutableLiveData<Event>()
    private val sessionCallback = object : ISessionCallback {
        override fun onSessionOpened() {
            requestMe()
        }

        override fun onSessionOpenFailed(exception: KakaoException?) {
            Logger.e("kakao onSessionOpenFailed : $exception")
        }
    }

    enum class Event {
        LOGIN_SUCCESS,
        LOGOUT,
        SESSION_CLOSED,
        NOT_SIGNED
    }

    private fun requestMe() {
        UserManagement.getInstance().me(object : MeV2ResponseCallback() {
            override fun onSuccess(result: MeV2Response?) {
                result?.let {
                    _me.value = KakaoUser(result.id, result.nickname)
                    _event.value = Event.LOGIN_SUCCESS
                    Logger.d("user id : $result")
                }
            }

            override fun onSessionClosed(errorResult: ErrorResult?) {
                logout()
                Logger.e("requestMe onSessionClosed : ${errorResult?.errorMessage}")
            }
        })
    }

    fun setSessionCallback() {
        Session.getCurrentSession().addCallback(sessionCallback)
        if (!Session.getCurrentSession().checkAndImplicitOpen()) {
            _event.value = Event.SESSION_CLOSED
            Logger.d("session closed")
        }
    }

    fun removeSessionCallback() {
        Session.getCurrentSession().removeCallback(sessionCallback)
    }

    fun logout() {
        UserManagement.getInstance().requestLogout(object : LogoutResponseCallback() {
            override fun onCompleteLogout() {
                _event.value = Event.LOGOUT
            }
        })
    }
}