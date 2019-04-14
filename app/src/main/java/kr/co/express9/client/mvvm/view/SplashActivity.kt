package kr.co.express9.client.mvvm.view

import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import kr.co.express9.client.R
import kr.co.express9.client.base.BaseActivity
import kr.co.express9.client.databinding.ActivitySplashBinding
import kr.co.express9.client.databinding.AlertAgreeWithTermsBinding
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
                    toast(R.string.kakao_login_success, kakaoViewModel.kakaoProfile.value?.nickname!!)
                    // DB에 유저 존재확인 > 메인화면으로 이동
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
