package kr.co.express9.client.util.extension

import com.google.gson.Gson

fun Any.AnyTostring(): String {
    return Gson().toJson(this)
}