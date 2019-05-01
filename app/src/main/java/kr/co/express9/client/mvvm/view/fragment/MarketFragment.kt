package kr.co.express9.client.mvvm.view.fragment

import kr.co.express9.client.R
import kr.co.express9.client.adapter.CategoryAdapter
import kr.co.express9.client.adapter.MarketAdapter
import kr.co.express9.client.base.BaseFragment
import kr.co.express9.client.databinding.FragmentMarketBinding
import kr.co.express9.client.mvvm.viewModel.MarketViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MarketFragment : BaseFragment<FragmentMarketBinding>(R.layout.fragment_market) {

    private val marketViewModel: MarketViewModel by viewModel()

    override fun initStartView() {
        val marketAdapter = MarketAdapter()
        dataBinding.marketAdapter = marketAdapter
        dataBinding.marketViewModel = marketViewModel

    }
}