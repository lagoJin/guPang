package kr.co.express9.client.mvvm.model.api

import io.reactivex.Single
import kr.co.express9.client.mvvm.model.data.Response
import kr.co.express9.client.mvvm.model.data.ResultNeedModify
import kr.co.express9.client.mvvm.model.data.User
import retrofit2.http.*


interface UserAPI {

    @GET("info")
    fun getInfo(@Query("userSeq") userSeq: Int): Single<Response<User>>

    @FormUrlEncoded
    @POST("login")
    fun login(@Field("uuid") uuid: String,
              @Field("name") name: String,
              @Field("deviceToken") deviceToken: String): Single<Response<String>>

    @FormUrlEncoded
    @POST("join")
    fun join(@Field("uuid") uuid: String,
             @Field("name") name: String,
             @Field("deviceToken") deviceToken: String): Single<Response<String>>

    @GET("favoriteMart")
    fun getFavoriteMart(@Query("userSeq") userSeq: Int = User.getUser().userSeq): Single<ResultNeedModify>

    @POST("favoriteMart")
    fun addFavoriteMart(@Field("userSeq") userSeq: Int = User.getUser().userSeq,
                        @Field("martSeq") martSeq: String): Single<ResultNeedModify>

    @DELETE("favoriteMart")
    fun deleteFavoriteMart(@Field("martSeq") martSeq: Int,
                           @Field("userSeq") userSeq: Int = User.getUser().userSeq): Single<ResultNeedModify>
}