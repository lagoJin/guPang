package kr.co.express9.client.mvvm.view


import android.content.Intent
import android.graphics.Typeface
import android.text.Editable
import android.text.Spannable
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.View
import androidx.lifecycle.Observer
import com.jakewharton.rxbinding3.widget.textChanges
import io.reactivex.android.schedulers.AndroidSchedulers
import kr.co.express9.client.R
import kr.co.express9.client.base.BaseActivity
import kr.co.express9.client.databinding.ActivityLocationBinding
import kr.co.express9.client.mvvm.view.fragment.MapFragment
import kr.co.express9.client.mvvm.viewModel.KakaoAddressViewModel
import kr.co.express9.client.mvvm.viewModel.UserViewModel
import kr.co.express9.client.util.Logger
import kr.co.express9.client.util.extension.launchActivity
import kr.co.express9.client.util.extension.toast
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

class LocationActivity : BaseActivity<ActivityLocationBinding>(R.layout.activity_location) {

    private val kakaoAddressViewModel: KakaoAddressViewModel by viewModel()
    private val userViewModel: UserViewModel by viewModel()

    override fun initStartView() {
        dataBinding.kakaoAddressViewModel = kakaoAddressViewModel
        dataBinding.userViewModel = userViewModel

        kakaoAddressViewModel.event.observe(this, Observer { event ->
            when (event) {
                KakaoAddressViewModel.Event.WRITE_SEARCH_ADDRESS -> {

                }
                KakaoAddressViewModel.Event.NO_ADDRESS -> {

                }
                KakaoAddressViewModel.Event.NETWORK_ERROR -> {
                    toast(R.string.network_error)
                }
            }
        })

        userViewModel.event.observe(this, Observer { event ->
            when (event) {
                UserViewModel.Event.LOGOUT -> {
                    launchActivity<LoginActivity> {
                        addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    }
                }
            }
        })

        dataBinding.tvLocationSetting.setOnClickListener {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, MapFragment())
                    .commitNow()
        }

        dataBinding.actvLocationAddress.setOnFocusChangeListener { view, b ->
            if (view.hasFocus()) {
                dataBinding.llLocationText.visibility = View.GONE
            } else {
                dataBinding.llLocationText.visibility = View.VISIBLE
            }
        }

        compositeDisposable.add(dataBinding.actvLocationAddress.textChanges()
                .filter { it.isNotEmpty() }
                .debounce(300, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ kakaoAddressViewModel.getAddressList(it as Editable) }, { throwable ->
                    Logger.e(throwable.localizedMessage)
                })
        )

        // 임시 코드
        dataBinding.bTemp.setOnClickListener {
            launchActivity<MainActivity> { }
        }

        dataBinding.bTemp2.setOnClickListener {
            userViewModel.logout()
        }
    }

    override fun onResume() {
        val span = dataBinding.tvLocationText.text as Spannable
        span.setSpan(StyleSpan(Typeface.BOLD), 11, 24, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        super.onResume()
    }
}