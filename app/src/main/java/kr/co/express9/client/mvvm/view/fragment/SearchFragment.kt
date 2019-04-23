package kr.co.express9.client.mvvm.view.fragment

import androidx.lifecycle.Observer
import kr.co.express9.client.R
import kr.co.express9.client.adapter.CategoryAdapter
import kr.co.express9.client.base.BaseFragment
import kr.co.express9.client.databinding.FragmentSearchBinding
import kr.co.express9.client.mvvm.viewModel.CategoryGoodsViewModel

class SearchFragment(
    private val categoryGoodsViewModel: CategoryGoodsViewModel
) : BaseFragment<FragmentSearchBinding>(R.layout.fragment_search) {

    override fun initStartView() {
        val categoryAdapter = CategoryAdapter()
        dataBinding.categoryAdapter = categoryAdapter
        categoryGoodsViewModel.goodsOrderByCategory.observe(this, Observer {
            categoryAdapter.goodsOrderByCategory = it
            categoryAdapter.notifyDataSetChanged()
        })
    }
}