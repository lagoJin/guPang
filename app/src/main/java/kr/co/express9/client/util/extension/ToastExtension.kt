package kr.co.express9.client.util.extension

import android.content.Context
import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.toast(msg: String, time: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(activity, msg, time).show()
}

fun Context.toast(msg: String, time: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msg, time).show()
}

fun Context.toast(msg: Int, time: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msg, time).show()
}

fun Context.toast(msg: Int, arg: String, time: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, String.format(this.getString(msg), arg), time).show()
}
