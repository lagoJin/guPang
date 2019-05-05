package kr.co.express9.client.mvvm.viewModel

import kr.co.express9.client.base.BaseViewModel
import kr.co.express9.client.mvvm.model.MarketRepository
import kr.co.express9.client.mvvm.model.enumData.StatusEnum
import kr.co.express9.client.util.extension.networkError
import org.koin.standalone.inject

class MarketViewModel : BaseViewModel<MarketViewModel.Event>() {

    private val marketRepository: MarketRepository by inject()

    enum class Event {
        MARKET_ADD,
        MARKET_DELETE
    }

    fun deleteFavoriteMart(martSeq: Int) {
        marketRepository.deleteFavoriteMarket(martSeq).subscribe(
            {
                if(it.status == StatusEnum.SUCCESS){

                    _event.value = Event.MARKET_DELETE
                }

            }, { throwable ->
                networkError(throwable)
            }
        ).apply {
            addDisposable(this)
        }
    }

}