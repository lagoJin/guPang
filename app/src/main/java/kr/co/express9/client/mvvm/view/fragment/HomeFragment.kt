package kr.co.express9.client.mvvm.view.fragment

import androidx.lifecycle.Observer
import kr.co.express9.client.R
import kr.co.express9.client.adapter.ItemTransformer
import kr.co.express9.client.adapter.LeafletAdapter
import kr.co.express9.client.adapter.ProductAdapter
import kr.co.express9.client.base.BaseFragment
import kr.co.express9.client.databinding.FragmentHomeBinding
import kr.co.express9.client.mvvm.view.MainActivity
import kr.co.express9.client.mvvm.viewModel.HomeViewModel
import kr.co.express9.client.mvvm.viewModel.MainViewModel
import kr.co.express9.client.util.extension.toast
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home) {

    private val homeViewModel: HomeViewModel by viewModel()
    private val mainViewModel: MainViewModel by sharedViewModel()

    override fun initStartView(isRestart: Boolean) {

        /**
         * dataBinding
         */
        val productAdapter = ProductAdapter(true)
        dataBinding.homeViewModel = homeViewModel
        dataBinding.productAdapter = productAdapter

        dataBinding.llOopsLayout.setOnClickListener {
            val mainActivity = activity as MainActivity
            mainActivity.setBottomNavigation(R.id.bn_market)
        }

        dataBinding.vpHome.apply {
            clipToPadding = false
            setItemTransformer(ItemTransformer())
            adapter = LeafletAdapter()
        }

        /**
         * mainViewModel
         * - sharedViewModel
         */
        mainViewModel.event.observe(this, Observer {event ->
            when(event) {
                MainViewModel.Event.CHANGE_FAVORITE_MART -> homeViewModel.getProducts()
            }
        })

        /**
         * homeViewModel
         */
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

        if (!isRestart) homeViewModel.getProducts()
    }
}