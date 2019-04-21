package kr.co.express9.client.mvvm.view

import androidx.fragment.app.Fragment
import kr.co.express9.client.R
import kr.co.express9.client.base.BaseActivity
import kr.co.express9.client.databinding.ActivityMainBinding
import kr.co.express9.client.mvvm.view.fragment.MainFragment
import kr.co.express9.client.mvvm.view.fragment.MartFragment
import kr.co.express9.client.mvvm.view.fragment.ProfileFragment
import kr.co.express9.client.mvvm.view.fragment.SearchFragment
import org.koin.android.ext.android.inject


class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val mainFragment: MainFragment by inject()
    private val searchFragment: SearchFragment by inject()
    private val martFragment: MartFragment by inject()
    private val profileFragment: ProfileFragment by inject()

    private lateinit var selectedFragment: Fragment

    override fun initStartView() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, mainFragment)
            .commitNow()

        dataBinding.bottomNavi.setOnNavigationItemSelectedListener { item ->
            selectedFragment = when (item.itemId) {
                R.id.main -> mainFragment
                R.id.search -> searchFragment
                R.id.mart -> martFragment
                R.id.profile -> profileFragment
                else -> mainFragment
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayout, selectedFragment)
                .commitNow()
            return@setOnNavigationItemSelectedListener true
        }
    }
}
