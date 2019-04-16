package kr.co.express9.client.mvvm.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kr.co.express9.client.base.BaseViewModel
import kr.co.express9.client.mvvm.model.UserRepository
import kr.co.express9.client.mvvm.model.data.User
import org.koin.standalone.inject

class UserViewModel : BaseViewModel() {

    private val userRepository: UserRepository by inject()

    enum class Event {
        NETWORK_ERROR
    }

    private val _event = MutableLiveData<Event>()
    val event: LiveData<Event> get() = _event

    private val _user = MutableLiveData<User>()
    val user: LiveData<User> get() = _user


    fun getUser() {
        _user.value = userRepository.getUser().value
    }
}