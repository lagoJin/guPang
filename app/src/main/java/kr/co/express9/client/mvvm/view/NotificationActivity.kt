package kr.co.express9.client.mvvm.view

import android.view.MenuItem
import androidx.lifecycle.Observer
import kr.co.express9.client.R
import kr.co.express9.client.adapter.NotificationHistoryAdapter
import kr.co.express9.client.base.BaseActivity
import kr.co.express9.client.databinding.ActivityNotificationBinding
import kr.co.express9.client.mvvm.viewModel.NotificationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class NotificationActivity : BaseActivity<ActivityNotificationBinding>(R.layout.activity_notification) {

    private val notificationViewModel: NotificationViewModel by viewModel()

    override fun initStartView(isRestart: Boolean) {
        val notificationHistoryAdapter = NotificationHistoryAdapter()
        dataBinding.notificationHistoryAdapter = notificationHistoryAdapter
        dataBinding.notificationViewModel = notificationViewModel
        notificationViewModel.notificationHistoryList.observe(this, Observer {
            notificationHistoryAdapter.notificationHistoryList = it
            notificationHistoryAdapter.notifyDataSetChanged()
        })

        // action bar 등록
        setSupportActionBar(dataBinding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if(!isRestart) notificationViewModel.getNotificationHistoryPref()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

}