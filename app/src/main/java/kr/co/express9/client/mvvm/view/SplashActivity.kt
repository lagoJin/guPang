package kr.co.express9.client.mvvm.view

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

    override fun initStartView() {
        kakaoUserViewModel.setSessionCallback()
        kakaoUserViewModel.event.observe(this, Observer { event ->
            when (event) {
                KakaoUserViewModel.Event.LOGIN -> {
                    // 우리 DB에 가입된 경우에만 통과로 수정해야함. 임시로 pref 존재시에 로그인 되도록 해둠
                    if (userViewModel.getPref() != null) {
                        toast(R.string.login_success, kakaoUserViewModel.kakaoProfile.value?.nickname!!)
                        launchActivity<GuideActivity>()
                    } else {
                        launchActivity<LoginActivity>()
                        kakaoUserViewModel.removeSessionCallback()
                    }
                }

                else -> launchActivity<LoginActivity>()
            }
            finish()
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        kakaoUserViewModel.removeSessionCallback()
    }
}
