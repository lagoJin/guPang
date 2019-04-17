package kr.co.express9.client.mvvm.view

import android.app.AlertDialog
import android.content.Intent
import androidx.lifecycle.Observer
import kr.co.express9.client.R
import kr.co.express9.client.base.BaseActivity
import kr.co.express9.client.databinding.ActivityGuideBinding
import kr.co.express9.client.mvvm.viewModel.GuideViewModel
import kr.co.express9.client.mvvm.viewModel.LogoutViewModel
import kr.co.express9.client.mvvm.viewModel.UserViewModel
import kr.co.express9.client.util.extension.launchActivity
import kr.co.express9.client.util.extension.toast
import org.koin.android.ext.android.inject

/**
 * 삭제될 예정
 */
class GuideActivity : BaseActivity<ActivityGuideBinding>(R.layout.activity_guide) {

    private val viewModel: GuideViewModel by inject()
    private val logoutViewModel: LogoutViewModel by inject()
    private val userViewModel: UserViewModel by inject()

    override fun initStartView() {
        dataBinding.model = viewModel
        dataBinding.userViewModel = userViewModel
        dataBinding.lifecycleOwner = this
        viewModel.event.observe(this, Observer { event ->
            when (event) {
                GuideViewModel.Event.WRITE_SEARCH_ADDRESS -> {
                    toast(R.string.write_search_address)
                }
                GuideViewModel.Event.NO_ADDRESS -> {
                    toast(R.string.no_address)
                }
                GuideViewModel.Event.NETWORK_ERROR -> {
                    toast(R.string.network_error)
                }
            }
        })


        logoutViewModel.event.observe(this, Observer { event ->
            when (event) {
                LogoutViewModel.Event.LOGOUT -> {
                    // 기기내 유저정보 삭제
                    userViewModel.deletePref {
                        toast(R.string.logout)
                        // LoginActivity로 이동

                        launchActivity<LoginActivity> {
                            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        }
                    }
                }
            }
        })

        dataBinding.btnLogout.setOnClickListener {
            AlertDialog.Builder(this)
                    .setMessage(getString(R.string.do_you_want_logout))
                    .setPositiveButton(R.string.yes) { alert, _ ->
                        logoutViewModel.logout()
                        alert.dismiss()
                    }
                    .setNegativeButton(R.string.no) { alert, _ ->
                        alert.dismiss()
                    }
                    .show()
        }

        dataBinding.btnCurrentPosition.setOnClickListener {
            launchActivity<MainActivity> {  }
        }
    }
}