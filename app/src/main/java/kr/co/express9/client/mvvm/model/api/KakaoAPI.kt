package kr.co.express9.client.mvvm.model.api

import com.kakao.usermgmt.response.MeV2Response
import io.reactivex.Single
import kr.co.express9.client.BuildConfig
import kr.co.express9.client.mvvm.model.data.Address
import retrofit2.http.*

interface KakaoAPI {

    @Headers("Authorization: ${BuildConfig.KAKAO_APP_KEY}")
    @GET("local/search/address.json?")
    fun getAddress(@Query("query") address: String): Single<Address>

    @FormUrlEncoded
    @Headers("Authorization: ${BuildConfig.KAKAO_ADMIN_KEY}")
    @POST("user/me")
    fun getUserProfile(@Field("target_id") uuid: String,
                       @Field("target_id_type") user_id: String = "user_id",
                       @Field("secure_resource") secure_resource: String = "true"): Single<MeV2Response>
}