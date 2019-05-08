package kr.co.express9.client.mvvm.model

import com.kakao.usermgmt.response.MeV2Response
import io.reactivex.Single
import kr.co.express9.client.mvvm.model.api.KakaoAPI
import kr.co.express9.client.mvvm.model.data.Address
import kr.co.express9.client.util.extension.netWorkSubscribe

class KakaoRepository(private val kakaoApi: KakaoAPI) {

    fun getAddress(searchAddress: String): Single<Address> {
        return kakaoApi.getAddress(searchAddress).netWorkSubscribe()
    }

    fun getUserProfile(uuid: String): Single<MeV2Response> {
        return kakaoApi.getUserProfile(uuid).netWorkSubscribe()
    }
}