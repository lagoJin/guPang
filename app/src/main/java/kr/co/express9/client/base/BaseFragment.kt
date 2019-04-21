package kr.co.express9.client.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import kr.co.express9.client.util.Logger

abstract class BaseFragment<T: ViewDataBinding>(private val layoutId: Int) : Fragment() {

    protected lateinit var dataBinding: T

    abstract fun initStartView()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dataBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), layoutId, null, false)
        Logger.d("onViewCreate ${this.javaClass.simpleName}")
        initStartView()
        return dataBinding.root
    }
}