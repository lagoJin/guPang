package kr.co.express9.client.mvvm.view

import kr.co.express9.client.R
import kr.co.express9.client.base.BaseActivity
import kr.co.express9.client.databinding.ActivityLeafletBinding
import kr.co.express9.client.mvvm.viewModel.LeafletViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class LeafletActivity : BaseActivity<ActivityLeafletBinding>(R.layout.activity_leaflet) {

    private val leafletViewModel: LeafletViewModel by viewModel()

    override fun initStartView() {
        setSupportActionBar(dataBinding.toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

}