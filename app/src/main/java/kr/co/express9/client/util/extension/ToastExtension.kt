package kr.co.express9.client.util.extension

import android.content.Context
import android.widget.Toast

fun toast(ctxt: Context, msg: String) {
    Toast.makeText(ctxt, msg, Toast.LENGTH_SHORT).show()
}

fun toast(ctxt: Context, msg: Int) {
    Toast.makeText(ctxt, ctxt.getString(msg), Toast.LENGTH_SHORT).show()
}

fun toast(ctxt: Context, msg: Int, arg: String) {
    Toast.makeText(ctxt, String.format(ctxt.getString(msg), arg), Toast.LENGTH_SHORT).show()
}

fun Context.toast(msg:String, time :Int = Toast.LENGTH_SHORT){
    Toast.makeText(this, msg, time).show()
}