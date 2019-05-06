package kr.co.express9.client.mvvm.view.fragment

import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.*
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_search.view.*
import kr.co.express9.client.R
import kr.co.express9.client.adapter.CategoryAdapter
import kr.co.express9.client.base.BaseFragment
import kr.co.express9.client.databinding.FragmentSearchBinding
import kr.co.express9.client.mvvm.viewModel.SearchViewModel
import kr.co.express9.client.util.Logger
import org.koin.android.ext.android.inject
import java.lang.reflect.Method


class SearchFragment : BaseFragment<FragmentSearchBinding>(R.layout.fragment_search) {
    private val searchViewModel: SearchViewModel by inject()

    private lateinit var sSetScrollPosition: Method
    private lateinit var sSelectTab: Method
    private var mPreviousScrollState: Int = 0
    private var mScrollState: Int = 0

    override fun initStartView(isRestart: Boolean) {
        val categoryAdapter = CategoryAdapter()
        dataBinding.categoryAdapter = categoryAdapter

        /**
         * searchViewModel
         */
        searchViewModel.categoryList.observe(this, Observer { categoryList ->
            categoryAdapter.categoryList = categoryList
            categoryAdapter.notifyDataSetChanged()
            categoryList.forEach {
                // 수량이 안올라감 확인 필요
                dataBinding.tablayout.addTab(dataBinding.tablayout.newTab().setText(it.name))
            }
        })

        /**
         * tablayout
         */
        try {
            sSetScrollPosition = TabLayout::class.java.getDeclaredMethod("setScrollPosition",
                    Int::class.javaPrimitiveType,
                    Float::class.javaPrimitiveType,
                    Boolean::class.javaPrimitiveType,
                    Boolean::class.javaPrimitiveType)
            sSetScrollPosition.isAccessible = true

            sSelectTab = TabLayout::class.java.getDeclaredMethod("selectTab",
                    TabLayout.Tab::class.java,
                    Boolean::class.javaPrimitiveType)
            sSelectTab.isAccessible = true
        } catch (e: NoSuchMethodException) {
            throw IllegalStateException("Can't reflect into method TabLayout" +
                    ".setScrollPosition(int, float, boolean, boolean)")
        }


        dataBinding.tablayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                // tab의 상태가 선택되지 않음에서 선택 상태로 변경됨
                dataBinding.viewpager.setCurrentItem(tab.position, true)
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                // 이미 선택된 상태의 tab이 사용자에 의해 다시 선택됨
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                // tab의 상태가 선택 상태에서 선택되지 않음으로 변경됨
            }

        })

        /**
         * viewpager
         */
        dataBinding.viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (dataBinding.tablayout.selectedTabPosition != position &&
                        position < dataBinding.tablayout.tabCount) {
                    // Select the tab, only updating the indicator if we're not being dragged/settled
                    // (since onPageScrolled will handle that).
                    val updateIndicator = mScrollState == SCROLL_STATE_IDLE ||
                            mScrollState == SCROLL_STATE_SETTLING &&
                            mPreviousScrollState == SCROLL_STATE_IDLE
                    selectTab(dataBinding.tablayout,
                            dataBinding.tablayout.getTabAt(position)!!,
                            updateIndicator)
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
                mPreviousScrollState = mScrollState
                mScrollState = state
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                /**
                 * Only update the text selection if we're not settling, or we are settling after
                 * being dragged
                 */
                val updateText = mScrollState != SCROLL_STATE_SETTLING || mPreviousScrollState == SCROLL_STATE_DRAGGING

                /**
                 * Update the indicator if we're not settling after being idle. This is caused
                 * from a setCurrentItem() call and will be handled by an animation from
                 * onPageSelected() instead.
                 */
                val updateIndicator = !(mScrollState == SCROLL_STATE_SETTLING &&
                        mPreviousScrollState == SCROLL_STATE_IDLE)
                setScrollPosition(position, positionOffset, updateText, updateIndicator)
            }
        })

        if (!isRestart) searchViewModel.searchProducts(null, null)
    }


    /**
     * synchronize viewpager scroll and tablayout indicator
     */
    private fun setScrollPosition(
            position: Int, positionOffset: Float, updateSelectedText: Boolean,
            updateIndicatorPosition: Boolean
    ) {
        try {
            sSetScrollPosition.invoke(
                    dataBinding.tablayout, position, positionOffset, updateSelectedText, updateIndicatorPosition
            )
        } catch (e: Exception) {
            Logger.d(e.toString())
        }

    }

    private fun selectTab(tabLayout: TabLayout, tab: TabLayout.Tab, updateIndicator: Boolean) {
        try {
            sSelectTab.invoke(tabLayout, tab, updateIndicator)
        } catch (e: Exception) {
            Logger.e(e.toString())
        }
    }
}