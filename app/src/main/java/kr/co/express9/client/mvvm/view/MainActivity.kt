package kr.co.express9.client.mvvm.view

import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kr.co.express9.client.R
import kr.co.express9.client.base.BaseActivity
import kr.co.express9.client.databinding.ActivityMainBinding
import kr.co.express9.client.mvvm.view.fragment.MainFragment
import kr.co.express9.client.mvvm.view.fragment.MartFragment
import kr.co.express9.client.mvvm.view.fragment.ProfileFragment
import kr.co.express9.client.mvvm.view.fragment.SearchFragment
import kr.co.express9.client.mvvm.viewModel.MainViewModel
import kr.co.express9.client.util.Logger
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val mainViewModel: MainViewModel by viewModel()

    private val mainFragment: MainFragment by inject()
    private val searchFragment: SearchFragment by inject()
    private val martFragment: MartFragment by inject()
    private val profileFragment: ProfileFragment by inject()

    private lateinit var selectedFragment: Fragment

    override fun initStartView() {
        dataBinding.bottomNavi.setOnNavigationItemSelectedListener { item ->
            Logger.d("클릭 : ${item.itemId}")
            mainViewModel.setSelectedItemId(item.itemId)
            return@setOnNavigationItemSelectedListener true
        }

        mainViewModel.selectedItemId.observe(this, Observer { selectedItemId ->
            Logger.d("관측 : $selectedItemId")
            setFragment(selectedItemId)
        })
    }

    private fun setFragment(selectedItemId: Int) {
        Logger.d("테스트 : $selectedItemId")
        selectedFragment = when (selectedItemId) {
            R.id.main -> mainFragment
            R.id.search -> searchFragment
            R.id.mart -> martFragment
            R.id.profile -> profileFragment
            else -> mainFragment
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout, selectedFragment)
            .commitNow()
    }

    override fun onDestroy() {
        Logger.d("디스트로이")
        super.onDestroy()
    }
}
