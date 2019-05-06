package kr.co.express9.client.mvvm.model.data

data class CartGoodsDummy (
    val product: Product,
    var total: Int = 0,
    var isHeader: Boolean = false,
    var isExpanded: Boolean = true,
    var isSelected: Boolean = false
)