package kr.co.express9.client.mvvm.model.preference

import androidx.lifecycle.MutableLiveData
import com.orhanobut.hawk.Hawk
import kr.co.express9.client.mvvm.model.data.Notification
import org.koin.standalone.KoinComponent

class NotificationPreferenceDataSource : KoinComponent {

    enum class NotificationPref(key: String) {
        NOTIFICATION_HISTORY("NOTIFICATION_HISTORY")
    }

    fun getNotificationHistory(): MutableLiveData<ArrayList<Notification>>? {
        return if (Hawk.contains(NotificationPref.NOTIFICATION_HISTORY.name)) {
            MutableLiveData<ArrayList<Notification>>().apply {
                value = Hawk.get(NotificationPref.NOTIFICATION_HISTORY.name)
            }
        } else {
            null
        }
    }

    fun addNotificationHistory(notification: Notification) {
        val notificationHistory: ArrayList<Notification> = Hawk.get(
            NotificationPref.NOTIFICATION_HISTORY.name,
            ArrayList()
        )
        ArrayList<Notification>().add(notification)
        Hawk.put(NotificationPref.NOTIFICATION_HISTORY.name, notificationHistory)
    }
}