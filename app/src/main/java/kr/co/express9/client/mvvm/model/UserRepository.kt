package kr.co.express9.client.mvvm.model

import androidx.lifecycle.MutableLiveData
import kr.co.express9.client.mvvm.model.data.User
import kr.co.express9.client.mvvm.model.remote.UserRemoteDataSource
import org.koin.standalone.inject

class UserRepository : UserDataSource {

    private val userRemoteDataSource: UserRemoteDataSource by inject()

    override fun getUser(): MutableLiveData<User> {
        return userRemoteDataSource.getUser()
    }

}