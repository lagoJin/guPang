package kr.co.express9.client.mvvm.model.data

import java.io.Serializable

data class Product(
        val productSeq: Int,
        val martSeq: Int,
        val category: String,
        val name: String,
        val saleUnitPrice: Int, // 임시값. 현재 api에서 안보냄
        val originalUnitPrice: Int,
        val detail: String,
        val startAt: String,
        val endAt: String,
        val status: String,
        val imageUrl: String
) : Serializable