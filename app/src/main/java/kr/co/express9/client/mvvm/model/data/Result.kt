package kr.co.express9.client.mvvm.model.data

import com.google.gson.annotations.Expose

data class Result(
    @Expose
    val result: List<Any>,
    val status: String
)