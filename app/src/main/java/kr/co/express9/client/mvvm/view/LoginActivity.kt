package kr.co.express9.client.mvvm.view

import androidx.lifecycle.Observer
import kr.co.express9.client.R
import kr.co.express9.client.base.BaseActivity
import kr.co.express9.client.databinding.ActivityLoginBinding
import kr.co.express9.client.mvvm.viewModel.LoginViewModel
import kr.co.express9.client.util.extension.launchActivity
import kr.co.express9.client.util.extension.toast
import org.koin.android.ext.android.inject


class LoginActivity : BaseActivity<ActivityLoginBinding, LoginViewModel>(R.layout.activity_login) {
    override val viewModel: LoginViewModel by inject()

    override fun initStartView() {
        viewDataBinding.btnMain.setOnClickListener {
            launchActivity<MainActivity>()
        }

        // kakao session
        viewModel.setSessionCallback()

        viewModel.event.observe(this, Observer { event ->
            when (event) {
                LoginViewModel.Event.KAKAO_LOGIN_SUCCESS -> {
                    toast(this, R.string.toast_kakao_login_success)
                }

                LoginViewModel.Event.KAKAO_LOGIN_FAILURE -> {
                    toast(this, R.string.toast_kakao_login_failure)
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.removeSessionCallback()
    }
}