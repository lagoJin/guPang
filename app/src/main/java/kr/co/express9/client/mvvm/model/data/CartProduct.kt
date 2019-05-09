package kr.co.express9.client.mvvm.model.data

data class CartProduct(
        // server data
        val productSeq: Int,
        var count: Int,
        val martSeq: Int,
        val martName: String,
        val productName: String,
        val originalUnitPrice: Int,
        val saleUnitPrice: Int,
        val imageUrl: String, // 임시로 해둠 향후 추가 될 예정
        // front data
        var isHeader: Boolean = false,
        var isExpanded: Boolean = false,
        var isSelected: Boolean = false
)