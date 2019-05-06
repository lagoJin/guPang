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
import kr.co.express9.client.util.Logger


class KakaoUserViewModel : BaseViewModel<KakaoUserViewModel.Event>() {

    enum class Event {
        LOGIN,
        LOGOUT,
        SESSION_OPEN_FAILED,
        SESSION_CLOSED
    }

    private val _kakaoProfile = MutableLiveData<MeV2Response>()
    val kakaoProfile: LiveData<MeV2Response>
        get() = _kakaoProfile

    private val sessionCallback = object : ISessionCallback {
        override fun onSessionOpened() {
            requestMe()
        }

        /**
         * 유저가 카카오 연결해제 후 세션 만료 전에 앱에 들어온 경우 호출됨
         */
        override fun onSessionOpenFailed(exception: KakaoException?) {
            _event.value = Event.SESSION_OPEN_FAILED
            Logger.e("kakao onSessionOpenFailed : $exception")
        }
    }

    /**
     * 카카오톡 productSeq, id 요청
     */
    private fun requestMe() {
        UserManagement.getInstance().me(object : MeV2ResponseCallback() {
            override fun onSuccess(result: MeV2Response?) {
                result?.let {
                    _kakaoProfile.value = result
                    _event.value = Event.LOGIN
                    Logger.d("requestMe success : $result")
                }
            }

            override fun onSessionClosed(errorResult: ErrorResult?) {
                _event.value = Event.SESSION_CLOSED
                Logger.e("requestMe onSessionClosed : ${errorResult?.errorMessage}")
            }
        })
    }

    /**
     * 네이티브앱 로그인시 호출
     * - 로그인 성공시 내부적으로 sessionCallback을 호출
     * - 웹 로그인시에는 바로 sessionCallback을 호출
     */
    fun handleActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)
    }

    /**
     * callback 등록 및 로그인 시도
     */
    fun setSessionCallback() {
        Session.getCurrentSession().addCallback(sessionCallback)
        if (!Session.getCurrentSession().checkAndImplicitOpen()) {
            _event.value = Event.SESSION_CLOSED
            Logger.d("session closed or has invalid token")
        }
    }

    /**
     * callback 삭제
     */
    fun removeSessionCallback() {
        Session.getCurrentSession().removeCallback(sessionCallback)
    }

    /**
     * 로그아웃
     */
    fun logout() {
        UserManagement.getInstance().requestLogout(object : LogoutResponseCallback() {
            override fun onCompleteLogout() {}

            override fun onSuccess(result: Long?) {
                _event.value = Event.LOGOUT
            }
        })
    }
}