package kr.co.express9.client.mvvm.model.api

import io.reactivex.Single
import kr.co.express9.client.mvvm.model.data.Notification
import kr.co.express9.client.mvvm.model.data.Response
import kr.co.express9.client.mvvm.model.data.User
import retrofit2.http.*

interface NotificationAPI {

    @GET("notification/")
    fun getNotifications(@Query("userSeq") userSeq: Int = User.getUser().userSeq): Single<Response<ArrayList<Notification>>>

    @FormUrlEncoded
    @POST("notification/")
    fun addNotification(@Field("text") text: String,
                        @Field("userSeq") userSeq: Int = User.getUser().userSeq): Single<Response<Unit>>

    @DELETE("notification/")
    fun deleteNotification(@Query("text") text: String,
                           @Query("userSeq") userSeq: Int = User.getUser().userSeq): Single<Response<Unit>>
}