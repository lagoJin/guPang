package kr.co.express9.client.mvvm.view

import android.app.Activity
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import kr.co.express9.client.R
import kr.co.express9.client.adapter.NotificationAdapter
import kr.co.express9.client.base.BaseActivity
import kr.co.express9.client.databinding.ActivityNotificationSettingBinding
import kr.co.express9.client.mvvm.viewModel.NotificationViewModel
import kr.co.express9.client.util.extension.toast
import org.koin.androidx.viewmodel.ext.android.viewModel


class NotificationSettingActivity : BaseActivity<ActivityNotificationSettingBinding>
    (R.layout.activity_notification_setting) {

    private val notificationViewModel: NotificationViewModel by viewModel()
    private lateinit var notificationAdapter: NotificationAdapter

    private val onDelete: (Int) -> Unit = {
        notificationViewModel.deleteNotification(it) { isSuccess ->
            if (isSuccess) toast(R.string.success_to_delete_notification)
        }
    }

    override fun initStartView(isRestart: Boolean) {
        notificationAdapter = NotificationAdapter(onDelete)
        dataBinding.notificationAdapter = notificationAdapter
        dataBinding.notificationViewModel = notificationViewModel
        dataBinding.bAddAlarm.setOnClickListener {
            notificationViewModel.addNotification { addNotification() }
        }

        dataBinding.etItemName.setOnEditorActionListener { _, _, _ ->
            notificationViewModel.addNotification { addNotification() }
            true
        }

        notificationViewModel.event.observe(this, Observer { event ->
            when (event) {
                NotificationViewModel.Event.ITEM_NAME_IS_NULL -> toast(R.string.please_write_the_notification_name)
                NotificationViewModel.Event.ALREADY_HAS_NOTIFICATION -> toast(R.string.already_has_notification)
            }
        })

        notificationViewModel.notificationList.observe(this, Observer {
            notificationAdapter.notificationList = it
            notificationAdapter.notifyDataSetChanged()

            // hide keyboard
            val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(dataBinding.etItemName.windowToken, 0)
        })

        // action bar 등록
        setSupportActionBar(dataBinding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (!isRestart) notificationViewModel.getNotifications()
    }

    private fun addNotification() {
        toast(R.string.success_to_add_notification)
        dataBinding.etItemName.setText("")
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}