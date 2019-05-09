package kr.co.express9.client.mvvm.model.data

data class CartHistory(
        val userSeq: Int,
        val purchaseYmd: String, // 201901
        val productSeq: Int,
        val martSeq: Int,
        val martName: String,
        val productName: String,
        val imageUrl: String, // 임시 이미지
        val originalUnitPrice: Int,
        val saleUnitPrice: Int,
        val count: Int,
        // front data
        var isHeader: Boolean = false,
        var totalPrice: Int,
        var itemPrice: Int
)