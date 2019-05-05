package kr.co.express9.client.mvvm.model.data

import com.google.gson.annotations.Expose
import kr.co.express9.client.mvvm.model.enumData.StatusEnum

// 지울 예정
data class ResultNeedModify(
    @Expose
    val result: Any,
    val status: StatusEnum
)