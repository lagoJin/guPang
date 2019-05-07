package kr.co.express9.client.thirdParty.firebase

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kr.co.express9.client.R
import kr.co.express9.client.util.Logger

class CloudMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        Logger.d("From: ${remoteMessage?.from}")
        remoteMessage?.data?.isNotEmpty()?.let {
            Logger.d("Message data payload:" + remoteMessage.data)
        }

        // Check if message contains a notification payload.
        remoteMessage?.notification?.let {
            Logger.d("Message Notification Body: ${it.body}")
            sendNotification(it)
        }
    }

    override fun onNewToken(token: String) {
        Logger.d("Refreshed token: $token")
        // sent registration to server
    }

    private fun sendNotification(remoteMessageNotification: RemoteMessage.Notification) {

        val channelId = getString(R.string.notification_channel_id)

        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(remoteMessageNotification.title)
                .setContentText(remoteMessageNotification.body)
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