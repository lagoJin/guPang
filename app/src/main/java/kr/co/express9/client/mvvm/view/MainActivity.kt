package kr.co.express9.client.mvvm.view

import android.view.Menu
import android.view.MenuItem
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kr.co.express9.client.R
import kr.co.express9.client.base.BaseActivity
import kr.co.express9.client.databinding.ActivityMainBinding
import kr.co.express9.client.mvvm.view.fragment.HomeFragment
import kr.co.express9.client.mvvm.view.fragment.MarketFragment
import kr.co.express9.client.mvvm.view.fragment.ProfileFragment
import kr.co.express9.client.mvvm.view.fragment.SearchFragment
import kr.co.express9.client.mvvm.viewModel.MainViewModel
import kr.co.express9.client.util.Logger
import kr.co.express9.client.util.extension.launchActivity
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val mainViewModel: MainViewModel by viewModel()

    private val homeFragment: HomeFragment by inject()
    private val searchFragment: SearchFragment by inject()
    private val marketFragment: MarketFragment by inject()
    private val profileFragment: ProfileFragment by inject()

    private lateinit var selectedFragment: Fragment
    private lateinit var toolbarMenu: Menu
    private var toolbarState = ToolbarState.MENU_IS_NOT_CREATED

    enum class ToolbarState {
        MENU_IS_NOT_CREATED,
        MENU_IS_CREATED
    }

    private lateinit var searchMenu: MenuItem

    override fun initStartView() {
        dataBinding.bottomNavigation.setOnNavigationItemSelectedListener { item ->
            if (toolbarState == ToolbarState.MENU_IS_CREATED) {
                if(searchMenu.isVisible) searchMenu.isVisible = false
                when (item.itemId) {
                    R.id.bn_home -> {
                        dataBinding.tvTitle.text = getString(R.string.magarine)
                    }
                    R.id.bn_search -> {
                        dataBinding.tvTitle.text = getString(R.string.menu_search)
                        searchMenu.isVisible = true
                    }
                    R.id.bn_market -> {
                        dataBinding.tvTitle.text = getString(R.string.menu_market)
                    }
                    R.id.bn_profile -> {
                        dataBinding.tvTitle.text = getString(R.string.menu_profile)
                    }
                }
            }
            mainViewModel.setSelectedItemId(item.itemId)
            return@setOnNavigationItemSelectedListener true
        }

        mainViewModel.selectedItemId.observe(this, Observer { selectedItemId ->
            setFragment(selectedItemId)
        })

        setSupportActionBar(dataBinding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (toolbarState == ToolbarState.MENU_IS_NOT_CREATED) {
            toolbarState = ToolbarState.MENU_IS_CREATED
            toolbarMenu = menu
        }
        menuInflater.inflate(R.menu.menu_search, toolbarMenu)
        menuInflater.inflate(R.menu.menu_cart, toolbarMenu)

        dataBinding.toolbar.menu.findItem(R.id.search).isVisible = false
        searchMenu = dataBinding.toolbar.menu.findItem(R.id.search)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.cart -> launchActivity<CartActivity>()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setFragment(selectedItemId: Int) {
        selectedFragment = when (selectedItemId) {
            R.id.bn_home -> homeFragment
            R.id.bn_search -> searchFragment
            R.id.bn_market -> marketFragment
            R.id.bn_profile -> profileFragment
            else -> homeFragment
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, selectedFragment)
            .commitNow()
    }
}
