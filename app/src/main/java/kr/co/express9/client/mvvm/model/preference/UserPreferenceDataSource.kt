package kr.co.express9.client.mvvm.model.preference

import androidx.lifecycle.MutableLiveData
import com.orhanobut.hawk.Hawk
import kr.co.express9.client.mvvm.model.data.User

class UserPreferenceDataSource {

    enum class UserPref(key: String) {
        USER("USER")
    }

    fun get(): MutableLiveData<User>? {
        return if (Hawk.contains(UserPref.USER.name)) {
            MutableLiveData<User>().apply {
                value = Hawk.get(UserPref.USER.name)
            }
        } else {
            null
        }
    }

    fun put(user: User) {
        Hawk.put(UserPref.USER.name, user)
    }

    fun delete() {
        Hawk.delete(UserPref.USER.name)
    }
}