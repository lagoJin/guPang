package kr.co.express9.client.mvvm.model.api

import io.reactivex.Single
import kr.co.express9.client.mvvm.model.data.Mart
import retrofit2.http.GET
import retrofit2.http.Query

interface MartAPI {

    @GET("")
    fun getMart(@Query("martSeq") martSeq: String): Single<Mart>

}