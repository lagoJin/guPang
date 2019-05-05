package kr.co.express9.client.mvvm.view

import android.os.Bundle
import androidx.lifecycle.Observer
import kr.co.express9.client.R
import kr.co.express9.client.base.BaseActivity
import kr.co.express9.client.databinding.ActivitySplashBinding
import kr.co.express9.client.mvvm.viewModel.KakaoUserViewModel
import kr.co.express9.client.mvvm.viewModel.UserViewModel
import kr.co.express9.client.util.extension.launchActivity
import kr.co.express9.client.util.extension.toast
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity : BaseActivity<ActivitySplashBinding>(R.layout.activity_splash) {

    private val kakaoUserViewModel: KakaoUserViewModel by viewModel()
    private val userViewModel: UserViewModel by viewModel()

    override fun initStartView(isRestart: Boolean) {
        kakaoUserViewModel.setSessionCallback()
        kakaoUserViewModel.event.observe(this, Observer { event ->
            when (event) {
                KakaoUserViewModel.Event.LOGIN -> {
                    userViewModel.checkIsOldUser(
                        kakaoUserViewModel.kakaoProfile.value?.id.toString(),
                        kakaoUserViewModel.kakaoProfile.value?.nickname.toString()
                    )
                }
                else -> launchLoginActivity()
            }
        })

        userViewModel.event.observe(this, Observer { event ->
            when (event) {
                UserViewModel.Event.OLD_USER -> {
                    toast(R.string.login_success, kakaoUserViewModel.kakaoProfile.value?.nickname!!)
                    launchActivity<LocationActivity>()
                    finish()
                }
                UserViewModel.Event.NEW_USER -> launchLoginActivity()
                else -> {
                }
            }
        })
    }

    private fun launchLoginActivity() {
        launchActivity<LoginActivity>()
        kakaoUserViewModel.removeSessionCallback()
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        kakaoUserViewModel.removeSessionCallback()
    }
}
