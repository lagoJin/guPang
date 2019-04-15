package kr.co.express9.client.mvvm.model.remote

import androidx.lifecycle.MutableLiveData
import kr.co.express9.client.mvvm.model.UserDataSource
import kr.co.express9.client.mvvm.model.data.User

class UserRemoteDataSource: UserDataSource {

    override fun getUser(): MutableLiveData<User> {
        // api가 없어서 임시로 만들음
        return MutableLiveData<User>().apply {
            value = User("1", "kakaoId", "magarine", "token")
        }
    }
}