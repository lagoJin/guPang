package kr.co.express9.client.mvvm.view.fragment

import androidx.lifecycle.Observer
import kr.co.express9.client.BR.marketAdapter
import kr.co.express9.client.R
import kr.co.express9.client.adapter.CategoryAdapter
import kr.co.express9.client.adapter.MarketAdapter
import kr.co.express9.client.base.BaseFragment
import kr.co.express9.client.databinding.FragmentMarketBinding
import kr.co.express9.client.mvvm.model.data.Mart
import kr.co.express9.client.mvvm.viewModel.MarketViewModel
import kr.co.express9.client.util.extension.toast
import org.koin.androidx.viewmodel.ext.android.viewModel

class MarketFragment : BaseFragment<FragmentMarketBinding>(R.layout.fragment_market) {

    private val marketViewModel: MarketViewModel by viewModel()

    private val martList = ArrayList<Mart>()

    override fun initStartView() {
        val marKetAdapter = MarketAdapter(martList) { i ->
            marketViewModel.deleteFavoriteMart(i)
        }.apply { this.notifyDataSetChanged() }

        dataBinding.marketAdapter = marKetAdapter
        dataBinding.marketViewModel = marketViewModel
        dataBinding.lifecycleOwner = this

        marketViewModel.event.observe(this, Observer { event ->
            when (event) {
                MarketViewModel.Event.MARKET_ADD -> {

                }
                MarketViewModel.Event.MARKET_DELETE -> {

                }
            }
        })


    }
}