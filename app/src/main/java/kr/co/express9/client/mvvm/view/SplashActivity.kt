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
                    toast(this, R.string.toast_kakao_login_success, kakaoViewModel.kakaoProfile.value?.nickname!!)
                    // DB 유저 토큰 갱신 API 추가 예정
                    // 갱신 성공시 메인화면으로 이동
                    launchActivity<MainActivity>()
                }
                else -> launchActivity<LoginActivity>()
            }
            finish()
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        kakaoViewModel.removeSessionCallback()
    }
}
