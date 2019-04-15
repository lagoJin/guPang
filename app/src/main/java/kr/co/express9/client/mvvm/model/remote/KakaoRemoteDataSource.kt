package kr.co.express9.client.mvvm.model.remote

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import kr.co.express9.client.mvvm.model.KakaoDataSource
import kr.co.express9.client.mvvm.model.api.KakaoAPI
import kr.co.express9.client.mvvm.model.data.AddressResponse
import org.koin.standalone.inject

class KakaoRemoteDataSource : KakaoDataSource {
    private val kakaoApi: KakaoAPI by inject()


    override fun getAddress(searchAddress: String): Single<AddressResponse> {
        return kakaoApi.getAddress(searchAddress)
            .subscribeOn(Schedulers.io())
    }
}