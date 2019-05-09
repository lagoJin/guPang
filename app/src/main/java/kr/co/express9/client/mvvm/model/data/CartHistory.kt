package kr.co.express9.client.mvvm.model.data

data class CartHistory(
        val userSeq: Int,
        val purchaseYmd: String, // 201901
        val productSeq: Int,
        val martSeq: Int,
        val martName: String,
        val productName: String,
        val originalUnitPrice: Int,
        val saleUnitPrice: Int,
        val count: Int
)