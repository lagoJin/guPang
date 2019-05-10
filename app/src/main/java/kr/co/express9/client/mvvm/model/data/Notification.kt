package kr.co.express9.client.mvvm.model.data

import java.io.Serializable

data class Notification (
        val text: String = "",
        val keyword: String = "",
        val martName: String = "",
        val productSeq: Int = 0,
        var getMilliseconds: Long
): Serializable