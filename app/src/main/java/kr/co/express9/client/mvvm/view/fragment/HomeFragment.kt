package kr.co.express9.client.mvvm.view.fragment

import kr.co.express9.client.R
import kr.co.express9.client.base.BaseFragment
import kr.co.express9.client.databinding.FragmentMainBinding
import kr.co.express9.client.mvvm.view.CartActivity
import kr.co.express9.client.util.extension.launchActivity

class HomeFragment: BaseFragment<FragmentMainBinding>(R.layout.fragment_main) {

    override fun initStartView() {

        dataBinding.btnCart.setOnClickListener {
            activity?.launchActivity<CartActivity>()
        }
    }
}