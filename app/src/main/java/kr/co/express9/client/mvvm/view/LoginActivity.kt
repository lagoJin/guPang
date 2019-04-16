package kr.co.express9.client.mvvm.view

import android.content.Intent
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import kr.co.express9.client.R
import kr.co.express9.client.base.BaseActivity
import kr.co.express9.client.databinding.ActivityLoginBinding
import kr.co.express9.client.databinding.AlertAgreeWithTermsBinding
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
                    toast(R.string.kakao_login_success, kakaoViewModel.kakaoProfile.value?.nickname!!)
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

    private fun getAgreeWithTerms() {
        val binding = DataBindingUtil.inflate<AlertAgreeWithTermsBinding>(
            LayoutInflater.from(this),
            R.layout.alert_agree_with_terms,
            null,
            false)
        AlertDialog.Builder(this)
            .setView(binding.root)
            .show()
    }
}