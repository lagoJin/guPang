package kr.co.express9.client.mvvm.view.fragment

import androidx.lifecycle.Observer
import kr.co.express9.client.R
import kr.co.express9.client.adapter.GoodsAdapter
import kr.co.express9.client.base.BaseFragment
import kr.co.express9.client.databinding.FragmentMainBinding
import kr.co.express9.client.mvvm.viewModel.HomeViewModel

class HomeFragment(
    private val homeViewModel: HomeViewModel
) : BaseFragment<FragmentMainBinding>(R.layout.fragment_main) {

    override fun initStartView() {
        val goodsAdapter = GoodsAdapter()
        goodsAdapter.showTitle = true
        homeViewModel.getGoods()
        homeViewModel.goods.observe(this, Observer {
            goodsAdapter.goodsList = it
            goodsAdapter.notifyDataSetChanged()
        })

        dataBinding.goodsAdapter = goodsAdapter

    }
}