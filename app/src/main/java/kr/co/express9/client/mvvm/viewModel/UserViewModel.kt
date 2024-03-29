package kr.co.express9.client.mvvm.viewModel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.kakao.usermgmt.UserManagement
import com.kakao.usermgmt.callback.LogoutResponseCallback
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import kr.co.express9.client.base.BaseViewModel
import kr.co.express9.client.mvvm.model.MartRepository
import kr.co.express9.client.mvvm.model.UserRepository
import kr.co.express9.client.mvvm.model.data.Response
import kr.co.express9.client.mvvm.model.data.User
import kr.co.express9.client.mvvm.model.enumData.StatusEnum
import kr.co.express9.client.util.Logger
import kr.co.express9.client.util.extension.anyTostring
import kr.co.express9.client.util.extension.getDeviceToken
import kr.co.express9.client.util.extension.toInteger
import org.koin.standalone.inject

class UserViewModel : BaseViewModel<UserViewModel.Event>() {

    private val userRepository: UserRepository by inject()
    private val martRepository: MartRepository by inject()

    enum class Event {
        NETWORK_ERROR,
        NEW_USER,
        LOGOUT,
        FAVORITE_MARTS_LOADED_SUCCESS,
        NO_FAVORITE_MARTS,
        FAVORITE_MARTS_LOADED_FAIL
    }

    private val _user by lazy { getPref() }
    val user: LiveData<User>
        get() = _user!!

    private val _progressView = MutableLiveData<Int>().apply { value = View.INVISIBLE }
    val progressView: LiveData<Int>
        get() = _progressView

    /**
     * 이미 가입한 유저인지 확인 (수정필요)
     * - old user인 경우 device token, id 갱신
     */
    fun checkIsOldUser(uuid: String, name: String, image: String?) {
        getDeviceToken()
                .flatMap {
                    userRepository.login(uuid, name, it)
                }
                .flatMap {
                    Logger.d("login : $it")

                    if (it.status == StatusEnum.SUCCESS) userRepository.getInfo(it.result.toInteger())
                    else {
                        _event.value = Event.NEW_USER
                        Single.error(Throwable("${it.result}"))
                    }
                }
                .doOnSubscribe { showProgress() }
                .subscribe({
                    // 이미 가입한 유저인지 확인
                    Logger.d("getInfo : $it")
                    if (it.status == StatusEnum.SUCCESS) {
                        it.result.image = image
                        putPref(it.result) { loadFavoriteMarts(it.result.userSeq) }
                    }
                    hideProgress()
                }, {
                    Logger.d(it.toString())
                    hideProgress()
                })
                .apply { addDisposable(this) }
    }

    /**
     * 회원가입 요청 (수정필요)
     * - new user인 경우 device token, id 갱신
     */
    fun signup(
            uuid: String,
            name: String,
            image: String?,
            isMarketingAgree: Boolean
    ) {
        // 회원가입
        getDeviceToken()
                .flatMap { userRepository.join(uuid, name, it) }
                .flatMap {
                    if (it.status == StatusEnum.SUCCESS) userRepository.getInfo(it.result.toInteger())
                    else Single.error(Throwable("${it.result}"))
                }
                .doOnSubscribe { showProgress() }
                .subscribe({
                    if (it.status == StatusEnum.SUCCESS) {
                        it.result.image = image
                        putPref(it.result) {
                            loadFavoriteMarts(it.result.userSeq)
                        }
                    }
                    hideProgress()
                }, {
                    Logger.d(it.toString())
                    hideProgress()
                })
                .apply { addDisposable(this) }
    }

    /**
     * 단골마트 불러오기
     * - preference 저장
     */
    fun loadFavoriteMarts(userSeq: Int) {
        martRepository.getFavoriteMarts(userSeq)
                .doOnSubscribe { showProgress() }
                .subscribe({
                    _event.value = if (it.status == StatusEnum.SUCCESS) {
                        // preference에 저장
                        Logger.d("내가 좋아요 마트 ${it.result.anyTostring()}")
                        if (it.result.size > 0) {
                            martRepository.putFavoriteMartsPref(it.result)
                            Event.FAVORITE_MARTS_LOADED_SUCCESS
                        } else {
                            Event.NO_FAVORITE_MARTS
                        }
                    } else Event.FAVORITE_MARTS_LOADED_FAIL
                    hideProgress()
                }, {
                    Logger.d(it.toString())
                    hideProgress()
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

    private fun showProgress() {
        _progressView.value = View.VISIBLE
    }

    private fun hideProgress() {
        _progressView.value = View.INVISIBLE
    }

}