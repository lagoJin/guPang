package kr.co.express9.client.mvvm.view.fragment

import android.view.View
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
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

        categoryGoodsViewModel.categoryList.observe(this, Observer { it ->
            it.forEach {
                dataBinding.tl.addTab(dataBinding.tl.newTab().setText(it.name))
            }
        })

        dataBinding.tl.addOnTabSelectedListener(object: TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                // tab의 상태가 선택되지 않음에서 선택 상태로 변경됨
                dataBinding.vp.currentItem = tab.position
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                // 이미 선택된 상태의 tab이 사용자에 의해 다시 선택됨
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                // tab의 상태가 선택 상태에서 선택되지 않음으로 변경됨
            }

        })

        dataBinding.vp.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                dataBinding.tl.getTabAt(position)?.select()
            }
        })
    }
}