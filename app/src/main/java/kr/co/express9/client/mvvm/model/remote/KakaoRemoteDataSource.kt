package kr.co.express9.client.mvvm.model.remote

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import kr.co.express9.client.mvvm.model.api.KakaoAPI
import kr.co.express9.client.mvvm.model.data.Address

class KakaoRemoteDataSource(private val kakaoApi: KakaoAPI) {

    fun getAddress(searchAddress: String): Single<Address> {
        return kakaoApi.getAddress(searchAddress)
            .subscribeOn(Schedulers.io())
    }
}