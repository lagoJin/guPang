package kr.co.express9.client.base

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kr.co.express9.client.util.Logger

abstract class BaseActivity<T : ViewDataBinding>(private val layoutId: Int) : AppCompatActivity() {

    companion object {
        private val arrayList = ArrayList<Activity>()
    }

    internal lateinit var dataBinding: T
    internal lateinit var compositeDisposable: CompositeDisposable

    abstract fun initStartView()

    internal fun clearActivity() {
        arrayList.clear()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, layoutId)
        compositeDisposable = CompositeDisposable()
        Logger.d("onCreate ${this.javaClass.simpleName}")
        arrayList.add(this)
        initStartView()
    }

    fun addDisposable(disposable: Disposable) {
        compositeDisposable.add(disposable)
    }

    override fun onDestroy() {
        arrayList.remove(this)
        compositeDisposable.clear()
        Logger.d("onDestroy ${this.javaClass.simpleName}")
        super.onDestroy()
    }
}