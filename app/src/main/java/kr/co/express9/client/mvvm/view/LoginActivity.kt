package kr.co.express9.client.mvvm.view

import android.content.Intent
import androidx.lifecycle.Observer
import kr.co.express9.client.R
import kr.co.express9.client.base.BaseActivity
import kr.co.express9.client.databinding.ActivityLoginBinding
import kr.co.express9.client.mvvm.viewModel.KakaoViewModel
import kr.co.express9.client.util.extension.launchActivity
import kr.co.express9.client.util.extension.toast
import org.koin.android.ext.android.inject


class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {
    private val kakaoViewModel: KakaoViewModel by inject()

    override fun initStartView() {
        // kakao session
        kakaoViewModel.setSessionCallback()
        kakaoViewModel.event.observe(this, Observer { event ->
            when (event) {
                KakaoViewModel.Event.LOGIN_SUCCESS -> {
                    toast(this, R.string.toast_kakao_login_success, kakaoViewModel.kakaoProfile.value?.nickname!!)
                    launchActivity<MainActivity>()
                    finish()
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        kakaoViewModel.removeSessionCallback()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        kakaoViewModel.handleActivityResult(requestCode, resultCode, data)
    }
}