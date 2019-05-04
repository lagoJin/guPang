package kr.co.express9.client.mvvm.model.data

import java.io.Serializable

data class GoodsDummy(
        val id: Int,
        val img: String,
        val name: String,
        val price: Int,
        val salePrice: Int,
        val market: String,
        val desc: String
) : Serializable