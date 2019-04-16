package kr.co.express9.client.mvvm.view

import android.app.AlertDialog
import android.content.Intent
import androidx.lifecycle.Observer
import kr.co.express9.client.R
import kr.co.express9.client.base.BaseActivity
import kr.co.express9.client.databinding.ActivityMainBinding
import kr.co.express9.client.mvvm.viewModel.LogoutViewModel
import kr.co.express9.client.mvvm.viewModel.MainViewModel
import kr.co.express9.client.util.extension.launchActivity
import kr.co.express9.client.util.extension.toast
import org.koin.android.ext.android.inject


class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val viewModel: MainViewModel by inject()
    private val logoutViewModel: LogoutViewModel by inject()

    override fun initStartView() {
        dataBinding.model = viewModel
        dataBinding.lifecycleOwner = this
        viewModel.event.observe(this, Observer { event ->
            when (event) {
                MainViewModel.Event.WRITE_SEARCH_ADDRESS -> {
                    toast(R.string.write_search_address)
                }
                MainViewModel.Event.NO_ADDRESS -> {
                    toast(R.string.no_address)
                }
                MainViewModel.Event.NETWORK_ERROR -> {
                    toast(R.string.network_error)
                }
            }
        })


        logoutViewModel.event.observe(this, Observer { event ->
            when (event) {
                LogoutViewModel.Event.LOGOUT -> {
                    // 기기내 유저정보 삭제
                    toast(R.string.logout)
                    // LoginActivity로 이동

                    launchActivity<LoginActivity> {
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
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

        }

        viewModel.user.observe(this, Observer {
            dataBinding.txtIntro.text = String.format(getString(R.string.intro_guide), it.nickname)
        })

        viewModel.getUser()
    }
}