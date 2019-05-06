package kr.co.express9.client.mvvm.model.data

import com.orhanobut.hawk.Hawk
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
            return Hawk.get("USER") as User
        }
    }

}