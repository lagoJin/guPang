package kr.co.express9.client.mvvm.model.api

import io.reactivex.Single
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MarketAPI {

    @GET("api/user/info")
    fun userInfo(@Query("userSeq") userSeq: String)

    @POST("api/user/login")
    fun loginUser(@Field("uuid") uuid: String, @Field("name") name: String, @Field("deviceToken") deviceToken: String): Single<Int>

}