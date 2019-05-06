package kr.co.express9.client.mvvm.model.api

import io.reactivex.Single
import kr.co.express9.client.mvvm.model.data.Mart
import kr.co.express9.client.mvvm.model.data.Response
import kr.co.express9.client.mvvm.model.data.ResultNeedModify
import retrofit2.http.*

interface MartAPI {

    @GET("info")
    fun getMart(@Query("martSeq") martSeq: Int): Single<Response<Mart>>

    @GET("search")
    fun getMarts(@Query("xx") xx: Double,
                 @Query("xy") xy: Double,
                 @Query("yx") yx: Double,
                 @Query("yy") yy: Double): Single<Response<List<Mart>>>

    @GET("favorite")
    fun getFavoriteMarts(@Query("userSeq") userSeq: Int): Single<Response<ArrayList<Mart>>>

    @POST("favorite")
    fun addFavoriteMart(@Query("userSeq") userSeq: Int,
                        @Query("martSeq") martSeq: Int): Single<Response<Unit>>

    @DELETE("favorite")
    fun deleteFavoriteMart(@Query("userSeq") userSeq: Int,
                           @Query("martSeq") martSeq: Int): Single<Response<Unit>>
}