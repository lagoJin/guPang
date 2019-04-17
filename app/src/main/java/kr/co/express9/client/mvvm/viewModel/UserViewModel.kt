package kr.co.express9.client.mvvm.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import kr.co.express9.client.base.BaseViewModel
import kr.co.express9.client.mvvm.model.UserRepository
import kr.co.express9.client.mvvm.model.data.User
import org.koin.standalone.inject

class UserViewModel : BaseViewModel() {

    private val userRepository: UserRepository by inject()

    enum class Event {
        NETWORK_ERROR,
        OLD_USER,
        NEW_USER,
        SIGNUP_SUCCESS
    }

    private val _event = MutableLiveData<Event>()
    val event: LiveData<Event> get() = _event

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    fun check(socialId: String, nickname: String, deviceToken: String) {
        // 이미 가입한 유저인지 확인(firebase 토큰, 닉네임 갱신) > 서버 요청
        // 이미 가입한 유저인 경우 > createPref & OLD_USER
        // 그렇지 않은 경우 > NEW_USER

        // 일단 임시로 pref에 있으면 old user로 판단
        if (get() == null) _event.value = Event.NEW_USER
        else _event.value = Event.OLD_USER
    }

    fun signup(socialId: String,
               nickname: String,
               deviceToken: String,
               isMarketingAgree: Boolean) {
        // 서버로 회원가입 요청 후 받은 유저 정보 pref에 저장
        val user = User("임시", socialId,nickname,deviceToken,isMarketingAgree)
        createPref(user ) {
            _event.value = Event.SIGNUP_SUCCESS
        }
    }

    fun get() = userRepository.getPref()?.value

    fun createPref(user: User, next: () -> Unit) {
        userRepository.createPref(user)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { next() }
                .apply { addDisposable(this) }
    }

    fun deletePref(next: () -> Unit) {
        userRepository.deletePref()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { next() }
                .apply { addDisposable(this) }
    }

}