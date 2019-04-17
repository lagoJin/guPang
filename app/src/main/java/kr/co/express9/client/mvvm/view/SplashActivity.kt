package kr.co.express9.client.mvvm.view

import androidx.lifecycle.Observer
import kr.co.express9.client.R
import kr.co.express9.client.base.BaseActivity
import kr.co.express9.client.databinding.ActivitySplashBinding
import kr.co.express9.client.mvvm.viewModel.KakaoViewModel
import kr.co.express9.client.mvvm.viewModel.UserViewModel
import kr.co.express9.client.util.extension.launchActivity
import kr.co.express9.client.util.extension.toast
import org.koin.android.ext.android.inject

class SplashActivity : BaseActivity<ActivitySplashBinding>(R.layout.activity_splash) {

    private val kakaoViewModel: KakaoViewModel by inject()
    private val userViewModel: UserViewModel by inject()

    override fun initStartView() {
        kakaoViewModel.setSessionCallback()
        kakaoViewModel.event.observe(this, Observer { event ->
            when (event) {
                KakaoViewModel.Event.LOGIN_SUCCESS -> {
                    // 우리 DB에 가입된 경우에만 통과로 수정해야함. 임시로 pref 존재시에 로그인 되도록 해둠
                    if (userViewModel.get() != null) {
                        toast(R.string.login_success, kakaoViewModel.kakaoProfile.value?.nickname!!)
                        launchActivity<GuideActivity>()
                    } else {
                        launchActivity<LoginActivity>()
                        kakaoViewModel.removeSessionCallback()
                    }
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
