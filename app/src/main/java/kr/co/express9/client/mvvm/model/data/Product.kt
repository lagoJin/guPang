package kr.co.express9.client.mvvm.model.data

import kr.co.express9.client.mvvm.model.enumData.CategoryEnum
import java.io.Serializable

data class Product(
        val productSeq: Int,
        val martSeq: String, //임시로 string으로 해 둠
        val category: CategoryEnum,
        val name: String,
        val price: Int, // 임시값. 현재 api에서 안보냄
        val unitPrice: Int,
        val detail: String,
        val startAt : String,
        val endAt : String,
        val status : String,
        val imageUrl: String
) : Serializable