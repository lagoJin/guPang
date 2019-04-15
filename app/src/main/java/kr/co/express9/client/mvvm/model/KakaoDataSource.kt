package kr.co.express9.client.mvvm.model

import io.reactivex.Single
import kr.co.express9.client.mvvm.model.data.AddressResponse
import org.koin.standalone.KoinComponent

interface KakaoDataSource: KoinComponent {

    fun getAddress(searchAddress: String): Single<AddressResponse>
}