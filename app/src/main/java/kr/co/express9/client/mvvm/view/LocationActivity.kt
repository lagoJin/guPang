package kr.co.express9.client.mvvm.view

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.Spannable
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.jakewharton.rxbinding3.widget.textChanges
import io.reactivex.android.schedulers.AndroidSchedulers
import kr.co.express9.client.R
import kr.co.express9.client.base.BaseActivity
import kr.co.express9.client.databinding.ActivityLocationBinding
import kr.co.express9.client.mvvm.view.fragment.MapFragment
import kr.co.express9.client.mvvm.viewModel.KakaoAddressViewModel
import kr.co.express9.client.mvvm.viewModel.UserViewModel
import kr.co.express9.client.util.Logger
import kr.co.express9.client.util.extension.anyTostring
import kr.co.express9.client.util.extension.hideKeyboard
import kr.co.express9.client.util.extension.launchActivity
import kr.co.express9.client.util.extension.toast
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

class LocationActivity : BaseActivity<ActivityLocationBinding>(R.layout.activity_location) {

    private val kakaoAddressViewModel: KakaoAddressViewModel by viewModel()
    private val userViewModel: UserViewModel by viewModel()

    override fun initStartView(isRestart: Boolean) {
        val adapter = ArrayAdapter(baseContext, android.R.layout.simple_dropdown_item_1line, kakaoAddressViewModel.addressResult.value!!)
        dataBinding.kakaoAddressViewModel = kakaoAddressViewModel
        dataBinding.userViewModel = userViewModel
        dataBinding.actvLocationAddress.setAdapter(adapter)
        dataBinding.lifecycleOwner = this

        kakaoAddressViewModel.event.observe(this, Observer { event ->
            when (event) {
                KakaoAddressViewModel.Event.WRITE_SEARCH_ADDRESS -> {

                }
                KakaoAddressViewModel.Event.SEARCH_SUCCESS -> {
                    adapter.clear()
                    adapter.addAll(kakaoAddressViewModel.addressResult.value!!)
                    adapter.notifyDataSetChanged()
                }
                KakaoAddressViewModel.Event.NO_ADDRESS -> {

                }
                KakaoAddressViewModel.Event.NETWORK_ERROR -> {
                    toast(kr.co.express9.client.R.string.network_error)
                }
                KakaoAddressViewModel.Event.NETWORK_SUCCESS -> {

                }
            }
        })

        dataBinding.actvLocationAddress.setOnItemClickListener { parent, view, position, id ->
            val document = kakaoAddressViewModel.realAdress.value!!.documents[position]
            val bundle = Bundle()
            bundle.putString("location", document.anyTostring())
            val map = MapFragment()
            map.arguments = bundle
            this.hideKeyboard()
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, map)
                    .commitNow()
        }

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

        dataBinding.llLocationSetting.setOnClickListener {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, MapFragment())
                    .commitNow()
        }

        dataBinding.actvLocationAddress.apply {

            setOnFocusChangeListener { view, b ->
                if (view.hasFocus()) {
                    dataBinding.llLocationText.animate().alpha(0.0f).setDuration(300).setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator?) {
                            super.onAnimationEnd(animation)
                            dataBinding.llLocationText.visibility = View.GONE
                        }
                    })
                } else {
                    dataBinding.llLocationText.animate().alpha(1.0f).setDuration(300).setListener(object : AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation: Animator?) {
                            super.onAnimationEnd(animation)
                            dataBinding.llLocationText.visibility = View.VISIBLE
                        }
                    })
                }
            }

        }

        dataBinding.actvLocationAddress.textChanges()
                .filter { it.isNotEmpty() }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ kakaoAddressViewModel.getAddressList(it as Editable) },
                        { throwable ->
                            Logger.e(throwable.localizedMessage)
                        }).apply { compositeDisposable.add(this) }

        // 임시 코드
        dataBinding.bTemp.setOnClickListener {
            launchActivity<MainActivity> { }
        }

        dataBinding.bTemp2.setOnClickListener {
            userViewModel.logout()
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        currentFocus?.let {
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
            dataBinding.llLocationText.visibility = View.VISIBLE
            dataBinding.actvLocationAddress.clearFocus()
        }
        return true
    }

    override fun onBackPressed() {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        currentFocus?.let {
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
            dataBinding.llLocationText.visibility = View.VISIBLE
            dataBinding.actvLocationAddress.clearFocus()
        }
        super.onBackPressed()
    }

    override fun onResume() {
        val span = dataBinding.tvLocationText.text as Spannable
        span.setSpan(StyleSpan(Typeface.BOLD), 11, 24, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        super.onResume()
    }

}