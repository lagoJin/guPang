package kr.co.express9.client.mvvm.view

import androidx.lifecycle.Observer
import kr.co.express9.client.R
import kr.co.express9.client.base.BaseActivity
import kr.co.express9.client.databinding.ActivityMainBinding
import kr.co.express9.client.mvvm.viewModel.MainViewModel
import kr.co.express9.client.util.extension.toast
import org.koin.android.ext.android.inject

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val viewModel: MainViewModel by inject()

    override fun initStartView() {
        dataBinding.model = viewModel
        dataBinding.lifecycleOwner = this
        viewModel.event.observe(this, Observer { event ->
            when (event) {
                MainViewModel.Event.WRITE_SEARCH_ADDRESS -> {
                    toast(R.string.write_search_address)
                }
                MainViewModel.Event.NO_ADDRESS -> {
                    toast(R.string.no_address)
                }
                MainViewModel.Event.NETWORK_ERROR -> {
                    toast(R.string.network_error)
                }
            }
        })
    }
}