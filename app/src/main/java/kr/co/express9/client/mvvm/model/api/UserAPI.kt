package kr.co.express9.client.mvvm.model.api

import io.reactivex.Single
import kr.co.express9.client.mvvm.model.data.Mart
import kr.co.express9.client.mvvm.model.data.Result
import kr.co.express9.client.mvvm.model.data.User
import retrofit2.http.*


interface UserAPI {

    @GET("info")
    fun userInfo(@Query("userSeq") userSeq: String)

    @POST("login")
    fun loginUser(@Field("uuid") uuid: String, @Field("name") name: String, @Field("deviceToken") deviceToken: String): Single<Result>

    @GET("favoriteMart")
    fun getFavoriteMart(@Query("userSeq") userSeq: String = User.getUser().userSeq): Single<Result>

    @POST("favoriteMart")
    fun addFavoriteMart(@Field("userSeq") userSeq: String = User.getUser().userSeq, @Field("martSeq") martSeq: String): Single<Result>

    @DELETE("favoriteMart")
    fun deleteFavoriteMart(@Field("userSeq") userSeq: String = User.getUser().userSeq, @Field("martSeq") martSeq: Int): Single<Result>
}