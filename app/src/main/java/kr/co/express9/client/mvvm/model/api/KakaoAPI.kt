package kr.co.express9.client.mvvm.model.api

import io.reactivex.Single
import kr.co.express9.client.constant.KAKAO_APP_KEY
import kr.co.express9.client.mvvm.model.data.AddressResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface KakaoAPI {

    @Headers("Authorization: $KAKAO_APP_KEY")
    @GET("local/search/address.json?")
    fun getAddress(@Query("query") address: String): Single<AddressResponse>
}