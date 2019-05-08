package kr.co.express9.client.mvvm.view

import androidx.lifecycle.Observer
import kr.co.express9.client.R
import kr.co.express9.client.base.BaseActivity
import kr.co.express9.client.databinding.ActivityNotificationSettingBinding
import kr.co.express9.client.mvvm.viewModel.NotificationViewModel
import kr.co.express9.client.util.extension.toast
import org.koin.androidx.viewmodel.ext.android.viewModel

class NotificationSettingActivity : BaseActivity<ActivityNotificationSettingBinding>
(R.layout.activity_notification_setting) {

    private val notificationViewModel: NotificationViewModel by viewModel()

    override fun initStartView(isRestart: Boolean) {

        dataBinding.notificationViewModel = notificationViewModel
        dataBinding.bAddAlarm.setOnClickListener {
            notificationViewModel.addNotification {
                toast(it.toString())
            }
        }

        notificationViewModel.event.observe(this, Observer { event ->
            when (event) {
                NotificationViewModel.Event.ITEM_NAME_IS_NULL -> {
                    toast(R.string.please_write_the_notification_name)
                }
            }
        })

        if(!isRestart) notificationViewModel.getNotifications()
    }
}