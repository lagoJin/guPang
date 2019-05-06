package kr.co.express9.client.mvvm.model

import androidx.lifecycle.LiveData
import io.reactivex.Single
import kr.co.express9.client.mvvm.model.api.MartAPI
import kr.co.express9.client.mvvm.model.api.UserAPI
import kr.co.express9.client.mvvm.model.data.Mart
import kr.co.express9.client.mvvm.model.data.Response
import kr.co.express9.client.mvvm.model.data.ResultNeedModify
import kr.co.express9.client.mvvm.model.preference.MartPreferenceDataSource
import kr.co.express9.client.util.extension.netWorkSubscribe
import kr.co.express9.client.util.extension.networkCommunication
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class MarketRepository : KoinComponent {

    private val userApi: UserAPI by inject()
    private val martAPI: MartAPI by inject()
    private val martPreferenceDataSource: MartPreferenceDataSource by inject()

    /**
     * Preference
     */
    fun getFavoriteMarts(): LiveData<ArrayList<Mart>>? {
        return martPreferenceDataSource.getFavoriteMarts()
    }

    fun putFavoriteMarts(favoriteMartList: ArrayList<Mart>) {
        martPreferenceDataSource.putFavoriteMarts(favoriteMartList)
    }

    fun deleteFavoriteMarts() {
        martPreferenceDataSource.deleteFavoriteMarts()
    }


    /**
     * Remote
     */
    fun deleteFavoriteMarket(martSeq: Int) : Single<ResultNeedModify> {
        return userApi.deleteFavoriteMart(martSeq = martSeq).netWorkSubscribe()
    }

    fun getMart(martSeq: Int): Single<Response<Mart>> {
        return martAPI.getMart(martSeq).networkCommunication()
    }

    fun getFavoriteMarts(userSeq: Int): Single<Response<ArrayList<Mart>>> {
        return martAPI.getFavoriteMarts(userSeq).networkCommunication()
    }

    fun addFavoriteMart(userSeq: Int, martSeq: Int): Single<Response<Unit>> {
        return martAPI.addFavoriteMart(userSeq, martSeq).networkCommunication()
    }

    fun deleteFavoriteMart(userSeq: Int, martSeq: Int): Single<Response<Unit>> {
        return martAPI.deleteFavoriteMart(userSeq, martSeq).networkCommunication()
    }

}