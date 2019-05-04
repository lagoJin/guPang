package kr.co.express9.client.mvvm.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.LogoutResponseCallback
import io.reactivex.android.schedulers.AndroidSchedulers
import kr.co.express9.client.base.BaseViewModel
import kr.co.express9.client.mvvm.model.UserRepository
import kr.co.express9.client.mvvm.model.data.User
import kr.co.express9.client.util.extension.getDeviceToken
import org.koin.standalone.inject

class UserViewModel : BaseViewModel<UserViewModel.Event>() {

    private val userRepository: UserRepository by inject()

    enum class Event{
        NETWORK_ERROR,
        OLD_USER,
        NEW_USER,
        SIGNUP_SUCCESS,
        LOGOUT
    }

    private val _user by lazy { getPref() }
    val user: LiveData<User>
        get() = _user!!

    /**
     * 이미 가입한 유저인지 확인 (수정필요)
     * - old user인 경우 device token, nickname 갱신
     */
    fun checkIsOldUser(socialId: String, nickname: String) {
        getDeviceToken()
            .subscribe { token ->
                // 이미 가입한 유저인지 확인 > 서버 요청 (추가예정)


                // 일단 임시로 pref에 있으면 old user로 판단
                if (getPref() == null) {
                    _event.value = Event.NEW_USER
                } else {
                    // 이미 가입한 유저인 경우(api로 받은 user preference에 저장)
                    putPref(
                        User(
                            "1",
                            socialId,
                            nickname,
                            token,
                            true
                        )
                    ) {
                        _event.value = Event.OLD_USER
                    }
                }
            }
            .apply { addDisposable(this) }
    }

    /**
     * 회원가입 요청 (수정필요)
     * - new user인 경우 device token, nickname 갱신
     */
    fun signup(
        socialId: String,
        nickname: String,
        deviceToken: String,
        isMarketingAgree: Boolean
    ) {
        // 서버로 회원가입 요청 후 받은 유저 정보 pref에 저장 > 서버 요청 (추가예정)
        val user = User("임시", socialId, nickname, deviceToken, isMarketingAgree)
        putPref(user) {

            _event.value = Event.SIGNUP_SUCCESS
        }
    }

    /**
     * 로그아웃
     * - preference 삭제
     */
    fun logout() {
        UserManagement.getInstance().requestLogout(object : LogoutResponseCallback() {
            override fun onCompleteLogout() {}

            override fun onSuccess(result: Long?) {
                deletePref { _event.value = Event.LOGOUT }
            }
        })
    }

    /**
     * preference에서 저장된 유저 불러오기
     */
    fun getPref() = userRepository.getPref()

    /**
     * preference에 유저 저장
     */
    fun putPref(user: User, next: () -> Unit) {
        userRepository.putPref(user)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { next() }
            .apply { addDisposable(this) }
    }

    /**
     * preference에 저장된 유저정보 삭제
     */
    fun deletePref(next: () -> Unit) {
        userRepository.deletePref()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { next() }
            .apply { addDisposable(this) }
    }

}