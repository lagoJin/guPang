package kr.co.express9.client.mvvm.view

import android.os.Handler
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
    private val hd = Handler()

    override fun initStartView(isRestart: Boolean) {
        kakaoUserViewModel.setSessionCallback()
        kakaoUserViewModel.event.observe(this, Observer { event ->
            when (event) {
                KakaoUserViewModel.Event.LOGIN -> {
                    userViewModel.checkIsOldUser(
                        kakaoUserViewModel.kakaoProfile.value?.id.toString(),
                        kakaoUserViewModel.kakaoProfile.value?.nickname.toString(),
                        kakaoUserViewModel.kakaoProfile.value?.profileImagePath
                    )
                }
                else -> launchLoginActivity()
            }
        })

        userViewModel.event.observe(this, Observer { event ->
            when (event) {
                UserViewModel.Event.NO_FAVORITE_MARTS -> {
                    hd.postDelayed({
                        toast(R.string.login_success, kakaoUserViewModel.kakaoProfile.value?.nickname!!)
                        launchActivity<LocationActivity>()
                        finish()
                    }, 1000)

                }
                UserViewModel.Event.FAVORITE_MARTS_LOADED_SUCCESS -> {
                    hd.postDelayed({
                        toast(R.string.login_success, kakaoUserViewModel.kakaoProfile.value?.nickname!!)
                        launchActivity<MainActivity>()
                        finish()
                    }, 1000)
                }
                UserViewModel.Event.NEW_USER -> launchLoginActivity()
                else -> {
                }
            }
        })
    }

    private fun launchLoginActivity() {
        kakaoUserViewModel.removeSessionCallback()
        hd.postDelayed({
            launchActivity<LoginActivity>()
            finish()
        }, 1000)
    }

    override fun onDestroy() {
        super.onDestroy()
        kakaoUserViewModel.removeSessionCallback()
    }
}
