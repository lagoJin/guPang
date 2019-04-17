package kr.co.express9.client.mvvm.model.remote

import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import kr.co.express9.client.mvvm.model.api.KakaoAPI
import kr.co.express9.client.mvvm.model.data.Address
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class KakaoRemoteDataSource : KoinComponent {

    private val kakaoApi: KakaoAPI by inject()

    fun getAddress(searchAddress: String): Single<Address> {
        return kakaoApi.getAddress(searchAddress)
            .subscribeOn(Schedulers.io())
    }
}