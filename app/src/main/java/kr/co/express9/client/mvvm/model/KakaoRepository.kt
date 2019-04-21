package kr.co.express9.client.mvvm.model

import kr.co.express9.client.mvvm.model.remote.KakaoRemoteDataSource
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class KakaoRepository : KoinComponent {

    private val kakaoRemoteDataSource: KakaoRemoteDataSource by inject()

    fun getAddress(searchAddress: String) = kakaoRemoteDataSource.getAddress(searchAddress)
}