package kr.co.express9.client.mvvm.model

import androidx.lifecycle.MutableLiveData
import kr.co.express9.client.mvvm.model.data.User
import org.koin.standalone.KoinComponent

interface UserDataSource: KoinComponent {

    fun getUser(): MutableLiveData<User>
}