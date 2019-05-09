package kr.co.express9.client.mvvm.model.api

import io.reactivex.Single
import kr.co.express9.client.mvvm.model.data.CartHistory
import kr.co.express9.client.mvvm.model.data.CartProduct
import kr.co.express9.client.mvvm.model.data.Response
import kr.co.express9.client.mvvm.model.data.User
import retrofit2.http.*

interface CartAPI {

    @FormUrlEncoded
    @POST("cart/item")
    fun addProduct(@Field("count") count: Int,
                   @Field("productSeq") productSeq: Int,
                   @Field("userSeq") userSeq: Int = User.getUser().userSeq): Single<Response<Unit>>

    @PUT("cart/count")
    fun changeAmount(@Field("count") count: Int,
                     @Field("productSeq") productSeq: Int,
                     @Field("userSeq") userSeq: Int = User.getUser().userSeq): Single<Response<Unit>>

    @GET("cart/item")
    fun getProducts(@Query("userSeq") userSeq: Int = User.getUser().userSeq): Single<Response<ArrayList<CartProduct>>>

    @DELETE("cart/item")
    fun deleteProduct(@Field("productSeq") productSeq: Int,
                      @Field("userSeq") userSeq: Int = User.getUser().userSeq): Single<Response<Unit>>

    @GET("purchase/history")
    fun getHistoryByMonth(@Query("purchaseYm") purchaseYm: String,
                          @Query("userSeq") userSeq: Int = User.getUser().userSeq): Single<Response<ArrayList<CartHistory>>>

    @FormUrlEncoded
    @POST("purchase/add")
    fun purchaseProduct(@Field("count") count: Int,
                        @Field("productSeq") productSeq: Int,
                        @Field("userSeq") userSeq: Int = User.getUser().userSeq): Single<Response<Unit>>
}