package kr.co.express9.client.mvvm.view.fragment

import androidx.lifecycle.Observer
import kr.co.express9.client.R
import kr.co.express9.client.adapter.MartAdapter
import kr.co.express9.client.base.BaseFragment
import kr.co.express9.client.databinding.FragmentMartBinding
import kr.co.express9.client.mvvm.model.data.Mart
import kr.co.express9.client.mvvm.model.data.User
import kr.co.express9.client.mvvm.viewModel.MartViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MartFragment : BaseFragment<FragmentMartBinding>(R.layout.fragment_mart) {

    private val martViewModel: MartViewModel by viewModel()

    private val martList = User.getFavoriteMarts()
    private lateinit var martAdapter: MartAdapter

    override fun initStartView(isRestart: Boolean) {
        martAdapter = MartAdapter(martList) { Mart ->
            martViewModel.deleteFavoriteMart(Mart.martSeq)
            martAdapter.notifyDataSetChanged()
        }

        dataBinding.martAdapter = martAdapter
        dataBinding.martViewModel = martViewModel
        dataBinding.lifecycleOwner = this

        martViewModel.event.observe(this, Observer { event ->
            when (event) {
                MartViewModel.Event.MART_ADD -> {

                }
                MartViewModel.Event.MART_DELETE -> {

                }
            }
        })

    }
}