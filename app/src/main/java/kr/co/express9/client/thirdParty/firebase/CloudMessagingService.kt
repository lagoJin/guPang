package kr.co.express9.client.thirdParty.firebase

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kr.co.express9.client.util.Logger

class CloudMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Logger.d("From: ${remoteMessage.from}")
        if (remoteMessage.data.isNotEmpty()) {
            Logger.d("Message data payload ${remoteMessage.data}")

//            if (/* Check if data needs to be processed by long running job */ true) {
//                // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
//                scheduleJob()
//            } else {
//                // Handle message within 10 seconds
//                handleNow()
//            }
        }

        // Check if message contains a notification payload.
        if (remoteMessage.notification != null) {
            Logger.d("Message Notification Body: ${remoteMessage.notification!!.body}")
        }
    }

    override fun onNewToken(token: String) {
        Logger.d("Refreshed token: $token")
        // sent registration to server
    }
}