package kr.co.express9.client.mvvm.model.api

import io.reactivex.Single
import kr.co.express9.client.mvvm.model.data.Mart
import kr.co.express9.client.mvvm.model.data.Result
import retrofit2.http.GET
import retrofit2.http.Query

interface MartAPI {

    @GET("")
    fun getMart(@Query("martSeq") martSeq: String): Single<Mart>

    @GET("search")
    fun getMarts(@Query("xx") xx: Double, @Query("xy") xy: Double, @Query("yx") yx: Double, @Query("yy") yy: Double): Single<Result>

}