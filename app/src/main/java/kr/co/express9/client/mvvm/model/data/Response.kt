package kr.co.express9.client.mvvm.model.data

import kr.co.express9.client.mvvm.model.enumData.StatusEnum

data class Response<T>(
        val status: StatusEnum,
        val result: T
)
