package kr.co.express9.client.util.extension

import com.google.firebase.iid.FirebaseInstanceId
import io.reactivex.Single
import kr.co.express9.client.util.Logger

fun getDeviceToken(): Single<String> {
    return Single.create {
        FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Logger.d("getInstanceId failed ${task.exception}")
                        it.onError(Throwable(task.exception))
                    }

                    // Get new Instance ID token
                    val token = task.result?.token
                    Logger.d("get device token : $token")
                    it.onSuccess(token!!)
                }
    }
}