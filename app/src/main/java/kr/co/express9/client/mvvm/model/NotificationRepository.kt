package kr.co.express9.client.mvvm.model

import io.reactivex.Single
import kr.co.express9.client.mvvm.model.api.NotificationAPI
import kr.co.express9.client.mvvm.model.data.Notification
import kr.co.express9.client.mvvm.model.data.Response
import kr.co.express9.client.util.extension.networkCommunication
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class NotificationRepository : KoinComponent {

    private val notificationAPI: NotificationAPI by inject()

    fun getNotifications(): Single<Response<ArrayList<Notification>>> {
        return notificationAPI.getNotifications().networkCommunication()
    }

    fun addNotification(text: String): Single<Response<Unit>> {
        return notificationAPI.addNotification(text).networkCommunication()
    }

    fun deleteNotification(text: String): Single<Response<Unit>> {
        return notificationAPI.deleteNotification(text).networkCommunication()
    }
}