package kr.co.express9.client.mvvm.view.fragment

import androidx.lifecycle.Observer
import kr.co.express9.client.R
import kr.co.express9.client.adapter.GoodsAdapter
import kr.co.express9.client.base.BaseFragment
import kr.co.express9.client.databinding.FragmentMainBinding
import kr.co.express9.client.mvvm.model.data.User
import kr.co.express9.client.mvvm.viewModel.HomeViewModel
import kr.co.express9.client.util.Logger
import kr.co.express9.client.util.extension.toast
import org.koin.android.ext.android.inject

class HomeFragment: BaseFragment<FragmentMainBinding>(R.layout.fragment_main) {
    private val homeViewModel: HomeViewModel by inject()

    override fun initStartView(isRestart: Boolean) {
        val goodsAdapter = GoodsAdapter()
        goodsAdapter.showTitle = true

        User.getFavoriteMarts().observe(this, Observer { marts ->
            Logger.d("User Observing :: Marts = $marts / Marts size = ${marts.size}")
            if(marts.size > 0) homeViewModel.getProducts()
            else {

            }
        })

        homeViewModel.event.observe(this, Observer { event ->
            when(event) {
                HomeViewModel.Event.NO_PRODUCTS -> {
                    toast("상품이 없습니다.")
                }
            }
        })
        homeViewModel.products.observe(this, Observer {
            goodsAdapter.productList = it
            goodsAdapter.notifyDataSetChanged()
        })

        dataBinding.goodsAdapter = goodsAdapter
//        if(!isRestart) homeViewModel.getProducts()
    }
}