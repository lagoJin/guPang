package kr.co.express9.client.mvvm.model.data

import java.io.Serializable

data class Mart(
    val createdAt: String,
    val detail: String,
    val imageUrl: String,
    val latitude: Double,
    val leafletImageUrl: String,
    val longitude: Double,
    val martSeq: Int,
    val name: String
) :Any(), Serializable