package kr.co.express9.client.mvvm.viewModel

import androidx.lifecycle.LiveData
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.LogoutResponseCallback
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import kr.co.express9.client.base.BaseViewModel
import kr.co.express9.client.mvvm.model.UserRepository
import kr.co.express9.client.mvvm.model.api.UserAPI
import kr.co.express9.client.mvvm.model.data.User
import kr.co.express9.client.mvvm.model.enumData.StatusEnum
import kr.co.express9.client.util.Logger
import kr.co.express9.client.util.extension.getDeviceToken
import org.koin.standalone.inject

class UserViewModel : BaseViewModel<UserViewModel.Event>() {

    private val userRepository: UserRepository by inject()
    private val userAPI: UserAPI by inject()

    enum class Event {
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
     * - old user인 경우 device token, name 갱신
     */
    fun checkIsOldUser(uuid: String, name: String) {
        getDeviceToken()
                .flatMap { userRepository.login(uuid, name, it) }
                .flatMap {
                    Logger.d("login : $it")
                    if (it.status == StatusEnum.SUCCESS) userRepository.getInfo(it.result)
                    else {
                        _event.value = Event.NEW_USER
                        Single.error(Throwable("${it.result}"))
                    }
                }
                .subscribe({
                    // 이미 가입한 유저인지 확인
                    Logger.d("getInfo : $it")
                    if (it.status == StatusEnum.SUCCESS) {
                        putPref(it.result) {
                            _event.value = Event.OLD_USER
                        }
                    }
                }, {
                    Logger.d(it.toString())
                })
                .apply { addDisposable(this) }
    }

    /**
     * 회원가입 요청 (수정필요)
     * - new user인 경우 device token, name 갱신
     */
    fun signup(
            uuid: String,
            name: String,
            isMarketingAgree: Boolean
    ) {
        // 회원가입
        getDeviceToken()
                .flatMap { userRepository.join(uuid, name, it) }
                .flatMap {
                    if (it.status == StatusEnum.SUCCESS) userRepository.getInfo(it.result)
                    else Single.error(Throwable("${it.result}"))
                }
                .subscribe({
                    if (it.status == StatusEnum.SUCCESS) {
                        putPref(it.result) { _event.value = Event.SIGNUP_SUCCESS }
                    }
                }, {
                    Logger.d(it.toString())
                })
                .apply { addDisposable(this) }
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
    private fun getPref() = userRepository.getPref()

    /**
     * preference에 유저 저장
     */
    private fun putPref(user: User, next: () -> Unit) {
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