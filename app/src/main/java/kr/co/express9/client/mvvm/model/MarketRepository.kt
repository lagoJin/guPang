package kr.co.express9.client.mvvm.model

import io.reactivex.Single
import kr.co.express9.client.mvvm.model.api.UserAPI
import kr.co.express9.client.mvvm.model.data.ResultNeedModify
import kr.co.express9.client.util.extension.netWorkSubscribe
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class MarketRepository : KoinComponent {

    private val userApi: UserAPI by inject()

    fun deleteFavoriteMarket(martSeq: Int) : Single<ResultNeedModify> {
        return userApi.deleteFavoriteMart(martSeq = martSeq).netWorkSubscribe()
    }

}