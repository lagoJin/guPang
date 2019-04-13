package kr.co.express9.client.mvvm.viewModel

import android.content.Intent
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

    val kakaoProfile: LiveData<KakaoUser> get() = _kakaoProfile
    val event: LiveData<Event> get() = _event

    private val _kakaoProfile = MutableLiveData<KakaoUser>()
    private val _event = MutableLiveData<Event>()
    private val sessionCallback = object : ISessionCallback {
        override fun onSessionOpened() {
            requestMe()
        }

        override fun onSessionOpenFailed(exception: KakaoException?) {
            // 유저가 카카오 연결해제 후 세션 만료 전에 앱에 들어온 경우 호출됨
            _event.value = Event.SESSION_OPEN_FAILED
            Logger.e("kakao onSessionOpenFailed : $exception")
        }
    }

    enum class Event {
        LOGIN_SUCCESS,
        LOGOUT,
        SESSION_OPEN_FAILED,
        SESSION_CLOSED,
        TOKEN_REFRESHED,
        NOT_SIGNED
    }

    private fun requestMe() {
        UserManagement.getInstance().me(object : MeV2ResponseCallback() {
            override fun onSuccess(result: MeV2Response?) {
                result?.let {
                    _kakaoProfile.value = KakaoUser(result.id, result.nickname)
                    checkUser()
                    Logger.d("requestMe success : $result")
                }
            }

            override fun onSessionClosed(errorResult: ErrorResult?) {
                logout()
                Logger.e("requestMe onSessionClosed : ${errorResult?.errorMessage}")
            }
        })
    }

    /**
     * 카카오에서 받은 id를 전달하여 유저 식별자 발급받은 후 MainActivity로 이동
     */
    private fun checkUser() {
        // API 추가 예정
        _event.value = Event.LOGIN_SUCCESS
    }

    /**
     * 네이티브앱 로그인시 호출되며, 로그인 성공시 내부적으로 sessionCallback을 호출함
     * 웹 로그인시에는 바로 sessionCallback을 호출함
     */
    fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)
    }

    fun setSessionCallback() {
        Session.getCurrentSession().addCallback(sessionCallback)
        if (!Session.getCurrentSession().checkAndImplicitOpen()) {
            _event.value = Event.SESSION_CLOSED
            Logger.d("session closed or has invalid token")
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