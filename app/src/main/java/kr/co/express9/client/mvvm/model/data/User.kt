package kr.co.express9.client.mvvm.model.data

import com.orhanobut.hawk.Hawk
import java.io.Serializable

data class User(
    val userSeq: String,
    val socialId: String,
    val nickname: String,
    val deviceToken: String,
    val isMarketingAgree: Boolean
) : Serializable {

    companion object {
        fun getUser(): User {
            return Hawk.get("USER") as User
        }
    }

}