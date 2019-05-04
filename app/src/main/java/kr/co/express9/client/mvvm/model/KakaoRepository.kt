package kr.co.express9.client.mvvm.model

import kr.co.express9.client.mvvm.model.remote.KakaoRemoteDataSource

class KakaoRepository(private val kakaoRemoteDataSource: KakaoRemoteDataSource) {

    fun getAddress(searchAddress: String) = kakaoRemoteDataSource.getAddress(searchAddress)
}