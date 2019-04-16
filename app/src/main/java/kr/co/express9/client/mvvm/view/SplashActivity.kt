package kr.co.express9.client.mvvm.view

import androidx.lifecycle.Observer
import kr.co.express9.client.R
import kr.co.express9.client.base.BaseActivity
import kr.co.express9.client.databinding.ActivitySplashBinding
import kr.co.express9.client.mvvm.viewModel.LoginViewModel
import kr.co.express9.client.util.extension.launchActivity
import kr.co.express9.client.util.extension.toast
import org.koin.android.ext.android.inject

class SplashActivity : BaseActivity<ActivitySplashBinding>(R.layout.activity_splash) {

    private val loginViewModel: LoginViewModel by inject()

    override fun initStartView() {
        loginViewModel.setSessionCallback()
        loginViewModel.event.observe(this, Observer { event ->
            when (event) {
                LoginViewModel.Event.LOGIN_SUCCESS -> {
                    toast(R.string.kakao_login_success, loginViewModel.kakaoProfile.value?.nickname!!)
                    launchActivity<MainActivity>()
                }

                else -> launchActivity<LoginActivity>()
            }
            finish()
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        loginViewModel.removeSessionCallback()
    }
}
