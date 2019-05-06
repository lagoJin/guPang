package kr.co.express9.client.mvvm.model.data

import java.io.Serializable

data class Category(
    val name: String,
    var total: Int = 0,
    val products: ArrayList<Product>
) : Serializable