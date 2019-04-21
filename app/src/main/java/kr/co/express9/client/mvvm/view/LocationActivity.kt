package kr.co.express9.client.mvvm.view


import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
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
import kr.co.express9.client.util.extension.toast
import org.koin.android.ext.android.inject
import java.util.concurrent.TimeUnit

class LocationActivity : BaseActivity<ActivityLocationBinding>(R.layout.activity_location) {

    private val kakaoAddressViewModel: KakaoAddressViewModel by inject()
    private val userViewModel: UserViewModel by inject()

    override fun initStartView() {
        dataBinding.kakaoAddressViewModel = kakaoAddressViewModel
        dataBinding.userViewModel = userViewModel
        dataBinding.lifecycleOwner = this

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

        dataBinding.tvLocationSetting.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MapFragment())
                .commitNow()
        }

        disposable.add(dataBinding.actvLocationAddress.textChanges()
            .filter { it.isNotEmpty() }
            .debounce(300, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ kakaoAddressViewModel.getAddressList(it as Editable) }, { throwable ->
                Logger.e(throwable.localizedMessage)

            })

        )

    }

    override fun onStop() {
        disposable.clear()
        super.onStop()
    }
}