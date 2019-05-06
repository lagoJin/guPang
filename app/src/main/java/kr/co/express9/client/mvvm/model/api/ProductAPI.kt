package kr.co.express9.client.mvvm.model.api

import io.reactivex.Single
import kr.co.express9.client.mvvm.model.data.Product
import kr.co.express9.client.mvvm.model.data.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductAPI {

    @GET("list")
    fun getProducts(@Query("martSeqList") martSeqList: String): Single<Response<ArrayList<Product>>>

    @GET("info")
    fun getProduct(@Query("productSeq") productSeq: String): Single<Response<Product>>

    @GET("search")
    fun searchProducts(@Query("martSeqList") martSeqList: String,
                   @Query("category") category: String,
                   @Query("name") name: String): Single<Response<ArrayList<Product>>>

}