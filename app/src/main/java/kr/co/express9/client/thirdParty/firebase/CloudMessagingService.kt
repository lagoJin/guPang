package kr.co.express9.client.thirdParty.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.google.gson.Gson
import kr.co.express9.client.R
import kr.co.express9.client.mvvm.model.NotificationRepository
import kr.co.express9.client.mvvm.model.data.Notification
import kr.co.express9.client.util.Logger
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class CloudMessagingService : FirebaseMessagingService(), KoinComponent {

    private val notificationRepository: NotificationRepository by inject()

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        Logger.d("From: ${remoteMessage?.from}")

        remoteMessage?.data?.let {
            Logger.d("Message data payload:" + remoteMessage.data)
//            val notification = Gson().fromJson(it.body.toString(), Notification::class.java)
        }

        // Check if message contains a notification payload.
        remoteMessage?.notification?.let {
            Logger.d("온다아아 $it")
            val notification = Gson().fromJson(it.body.toString(), Notification::class.java)
            sendNotification(notification)
        }
    }

    override fun onNewToken(token: String) {
        Logger.d("Refreshed token: $token")
        // sent registration to server
    }

    private fun sendNotification(notification: Notification) {
        notificationRepository.addNotificationHistoryPref(notification)

        val channelId = getString(R.string.notification_channel_id)

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val title = getString(R.string.noti_title, notification.keyword)
        val body = getString(R.string.noti_body, notification.martName)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)


        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            val channelName = getString(R.string.app_name)
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }
}