package kr.co.express9.client.mvvm.model.data

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
        fun getUser(): User {
            return Hawk.get(UserPreferenceDataSource.UserPref.USER.name)
        }

        fun getFavoriteMarts(): ArrayList<Mart> {
            return Hawk.get(MartPreferenceDataSource.MartPref.FAVORITE_MARTS.name)
        }

        fun getFavoriteMartsSeqList():String {
            val martSeqList = ArrayList<Int>()
            getFavoriteMarts().forEach { martSeqList.add(it.martSeq) }
            return martSeqList.joinToString()
        }
    }

}