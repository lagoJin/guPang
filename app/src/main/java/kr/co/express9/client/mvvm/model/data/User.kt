package kr.co.express9.client.mvvm.model.data

data class User(
    val userSeq: String,
    val socialId: String,
    val nickname: String,
    val deviceToken: String
)