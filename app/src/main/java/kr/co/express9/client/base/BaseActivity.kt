package kr.co.express9.client.base

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import kr.co.express9.client.util.Logger

abstract class BaseActivity<T: ViewDataBinding>(private val layoutId: Int): AppCompatActivity() {

    companion object {
        private val arrayList = ArrayList<Activity>()
    }

    protected lateinit var dataBinding: T

    abstract fun initStartView()

    internal fun clearActivity() {
        arrayList.clear()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, layoutId)
        Logger.d("onCreate ${this.javaClass.simpleName}")
        arrayList.add(this)
        initStartView()
    }

    override fun onDestroy() {
        arrayList.remove(this)
        Logger.d("onDestroy ${this.javaClass.simpleName}")
        super.onDestroy()
    }
}