package kr.co.express9.client.mvvm.view

import androidx.lifecycle.Observer
import kr.co.express9.client.R
import kr.co.express9.client.base.BaseActivity
import kr.co.express9.client.databinding.ActivityMainBinding
import kr.co.express9.client.mvvm.viewModel.MainViewModel
import kr.co.express9.client.util.extension.toast
import org.koin.android.ext.android.inject

class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel>(R.layout.activity_main) {

    override val viewModel: MainViewModel by inject()

    override fun initStartView() {
        viewDataBinding.model = viewModel
        viewDataBinding.lifecycleOwner = this
        viewModel.event.observe(this, Observer { event ->
            when (event) {
                MainViewModel.Event.WRITE_SEARCH_ADDRESS -> {
                    toast(this, R.string.toast_write_search_address)
                }
                MainViewModel.Event.NO_ADDRESS -> {
                    toast(this, R.string.toast_no_address)
                }
                MainViewModel.Event.NETWORK_ERROR -> {
                    toast(this, R.string.toast_network_error)
                }
            }
        })
    }
}