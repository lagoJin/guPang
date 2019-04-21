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
import kr.co.express9.client.mvvm.viewModel.KakaoUserViewModel
import kr.co.express9.client.mvvm.viewModel.TermsViewModel
import kr.co.express9.client.mvvm.viewModel.UserViewModel
import kr.co.express9.client.util.extension.launchActivity
import kr.co.express9.client.util.extension.toast
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : BaseActivity<ActivityLoginBinding>(R.layout.activity_login) {

    private val kakaoUserViewModel: KakaoUserViewModel by viewModel()
    private val userViewModel: UserViewModel by viewModel()
    private val termsViewModel: TermsViewModel by viewModel()

    private lateinit var alertDialog: AlertDialog

    override fun initStartView() {
        // kakao session
        kakaoUserViewModel.setSessionCallback()
        kakaoUserViewModel.event.observe(this, Observer { event ->
            when (event) {
                // 1. 카카오톡 로그인(카카오톡id, 닉네임 발급)
                KakaoUserViewModel.Event.LOGIN -> {
                    // 2. 이미 가입한 유저인지 확인(firebase 토큰, 닉네임 갱신)
                    userViewModel.checkIsOldUser(
                            kakaoUserViewModel.kakaoProfile.value?.id.toString(),
                            kakaoUserViewModel.kakaoProfile.value?.nickname.toString(),
                            "향후 추가 예정"
                    )
                }
                else -> {
                }
            }
        })

        userViewModel.event.observe(this, Observer { event ->
            when (event) {
                UserViewModel.Event.OLD_USER -> launchActivity() // 2.1 이미 가입한 유저인 경우 유저키, 닉네임 preference에 저장
                UserViewModel.Event.NEW_USER -> getAgreeWithTerms() // 3. 약관 동의
                UserViewModel.Event.SIGNUP_SUCCESS -> launchActivity() // 5. 회원가입 완료 및 메인으로
                else -> {
                }
            }
        })

        termsViewModel.event.observe(this, Observer { event ->
            when (event) {
                TermsViewModel.Event.AGREE -> { // 4. 회원가입(카카오톡id, 닉네임, firebase 토큰)
                    userViewModel.signup(
                            kakaoUserViewModel.kakaoProfile.value?.id.toString(),
                            kakaoUserViewModel.kakaoProfile.value?.nickname.toString(),
                            "임시토큰",
                            termsViewModel.marketingAgreement.value!!
                    )
                    alertDialog.dismiss()
                }
                TermsViewModel.Event.DISAGREE -> toast(R.string.you_cannot_signup_without_agreement)
                else -> {
                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        kakaoUserViewModel.removeSessionCallback()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        kakaoUserViewModel.handleActivityResult(requestCode, resultCode, data)
    }

    /**
     * 개인정보 이용약관
     */
    private fun getAgreeWithTerms() {
        val binding = DataBindingUtil.inflate<AlertAgreeWithTermsBinding>(
                LayoutInflater.from(this),
                R.layout.alert_agree_with_terms,
                null,
                false
        )
        binding.termsViewModel = termsViewModel
        alertDialog = AlertDialog.Builder(this)
                .setView(binding.root).create()

        alertDialog.show()
    }

    private fun launchActivity() {
        toast(R.string.login_success, kakaoUserViewModel.kakaoProfile.value?.nickname!!)
        launchActivity<LocationActivity>()
        finish()
    }
}