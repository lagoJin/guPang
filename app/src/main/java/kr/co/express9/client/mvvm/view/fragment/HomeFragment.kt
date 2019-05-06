package kr.co.express9.client.mvvm.view.fragment

import androidx.lifecycle.Observer
import kr.co.express9.client.R
import kr.co.express9.client.adapter.ProductAdapter
import kr.co.express9.client.base.BaseFragment
import kr.co.express9.client.databinding.FragmentHomeBinding
import kr.co.express9.client.mvvm.model.data.User
import kr.co.express9.client.mvvm.view.MainActivity
import kr.co.express9.client.mvvm.viewModel.HomeViewModel
import kr.co.express9.client.util.Logger
import kr.co.express9.client.util.extension.toast
import org.koin.android.ext.android.inject

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {
    private val homeViewModel: HomeViewModel by inject()

    override fun initStartView(isRestart: Boolean) {
        val productAdapter = ProductAdapter(true)
        dataBinding.homeViewModel = homeViewModel
        dataBinding.productAdapter = productAdapter

        homeViewModel.event.observe(this, Observer { event ->
            when (event) {
                HomeViewModel.Event.NO_PRODUCTS -> {
                    toast("상품이 없습니다.")
                }
            }
        })
        homeViewModel.products.observe(this, Observer {
            productAdapter.productList = it
            productAdapter.notifyDataSetChanged()
        })

        dataBinding.llOopsLayout.setOnClickListener {
            val mainActivity = activity as MainActivity
            mainActivity.setBottomNavigation(R.id.bn_market)
        }

        if(!isRestart) homeViewModel.getProducts()
    }
}