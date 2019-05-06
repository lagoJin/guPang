package kr.co.express9.client.mvvm.model.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.orhanobut.hawk.Hawk
import kr.co.express9.client.mvvm.model.preference.MartPreferenceDataSource
import kr.co.express9.client.mvvm.model.preference.UserPreferenceDataSource
import java.io.Serializable

data class User(
    val userSeq: Int,
    val uuid: String,
    val name: String,
    val deviceToken: String,
    val isMarketingAgree: Boolean = true // 서버쪽에 아직 구현이 안됨
) : Serializable {

    companion object {
        fun getUser(): LiveData<User> {
            return MutableLiveData<User>().apply {
                Hawk.get(UserPreferenceDataSource.UserPref.USER.name)
            }
        }

        fun getFavoriteMarts(): LiveData<ArrayList<Mart>> {
            return MutableLiveData<ArrayList<Mart>>().apply {
                Hawk.get(MartPreferenceDataSource.MartPref.FAVORITE_MARTS.name)
            }
        }
    }

}