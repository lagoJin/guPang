package kr.co.express9.client.mvvm.viewModel

import kr.co.express9.client.base.BaseViewModel
import kr.co.express9.client.mvvm.model.MarketRepository
import kr.co.express9.client.util.extension.networkError
import org.koin.standalone.inject

class MarketViewModel : BaseViewModel<MarketViewModel.Event>() {

    private val marketRepository: MarketRepository by inject()

    enum class Event {
        MARKET_DELETE
    }

    fun deleteFavoriteMart(martSeq: Int) {
        marketRepository.deleteFavoriteMarket(martSeq).subscribe(
            { result ->

            }, { throwable ->
                networkError(throwable)
            }
        ).apply {
            addDisposable(this)
        }
    }

}