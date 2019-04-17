package kr.co.express9.client.mvvm.model.data


data class Address (
    val meta: Meta,
    val documents: ArrayList<Document>
) {
    data class Meta(
        val total_count: Int,
        val pageable_count: Int,
        val is_end: Boolean
    )

    data class Document(
        val address_name: String,
        val y: Double,
        val x: Double
    )
}