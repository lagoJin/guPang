package kr.co.express9.client.mvvm.view

import androidx.lifecycle.Observer
import kr.co.express9.client.R
import kr.co.express9.client.base.BaseActivity
import kr.co.express9.client.databinding.ActivitySplashBinding
import kr.co.express9.client.mvvm.viewModel.KakaoViewModel
import kr.co.express9.client.util.extension.launchActivity
import kr.co.express9.client.util.extension.toast
import org.koin.android.ext.android.inject

class SplashActivity : BaseActivity<ActivitySplashBinding>(R.layout.activity_splash) {

    private val kakaoViewModel: KakaoViewModel by inject()

    override fun initStartView() {
        kakaoViewModel.setSessionCallback()
        kakaoViewModel.event.observe(this, Observer { event ->
            when (event) {
                KakaoViewModel.Event.LOGIN_SUCCESS -> {
                    toast(this, R.string.toast_kakao_login_success, kakaoViewModel.me.value?.nickname!!)
                    // DB에 유저 존재여부 확인 후 메인으로 이동
                    launchActivity<MainActivity>()
                }

                KakaoViewModel.Event.SESSION_CLOSED -> {
                    launchActivity<LoginActivity>()
                }
            }
            finish()
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        kakaoViewModel.removeSessionCallback()
    }
}
