package kr.co.express9.client.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import io.reactivex.disposables.CompositeDisposable
import kr.co.express9.client.util.Logger

abstract class BaseFragment<T : ViewDataBinding>(private val layoutId: Int) : Fragment() {

    internal lateinit var dataBinding: T
    internal lateinit var compositeDisposable: CompositeDisposable

    abstract fun initStartView(isRestart: Boolean)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        Logger.d("onViewCreate ${this.javaClass.simpleName}")
        dataBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), layoutId, null, false)
        dataBinding.lifecycleOwner = this
        compositeDisposable = CompositeDisposable()
        val isRestart = savedInstanceState != null
        initStartView(isRestart)
        return dataBinding.root
    }

    override fun onDestroy() {
        compositeDisposable.clear()
        super.onDestroy()
    }
}
