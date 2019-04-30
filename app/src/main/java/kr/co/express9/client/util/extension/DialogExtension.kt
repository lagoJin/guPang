package kr.co.express9.client.util.extension

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import kr.co.express9.client.R

fun<T : ViewDataBinding> Context.dialog(layoutId: Int, callback:(AlertDialog, T) -> Unit) {
    val binding = DataBindingUtil.inflate<T>(
        LayoutInflater.from(this),
        layoutId,
        null,
        false
    )

    val dialog = AlertDialog.Builder(this, R.style.CustomDialog).create()
    dialog.setView(binding.root)
    callback(dialog, binding)
}