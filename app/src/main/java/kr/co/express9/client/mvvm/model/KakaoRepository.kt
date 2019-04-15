package kr.co.express9.client.mvvm.model

import io.reactivex.Single
import kr.co.express9.client.mvvm.model.data.AddressResponse
import kr.co.express9.client.mvvm.model.remote.KakaoRemoteDataSource
import org.koin.standalone.inject

class KakaoRepository : KakaoDataSource {

    private val kakaoRemoteDataSource: KakaoRemoteDataSource by inject()

    override fun getAddress(searchAddress: String): Single<AddressResponse> {
        return kakaoRemoteDataSource.getAddress(searchAddress)
    }


}