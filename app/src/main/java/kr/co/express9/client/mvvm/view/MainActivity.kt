package kr.co.express9.client.mvvm.view

import android.app.SearchManager
import android.graphics.Color
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.cursoradapter.widget.SimpleCursorAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kr.co.express9.client.R
import kr.co.express9.client.base.BaseActivity
import kr.co.express9.client.databinding.ActivityMainBinding
import kr.co.express9.client.mvvm.view.fragment.HomeFragment
import kr.co.express9.client.mvvm.view.fragment.MartFragment
import kr.co.express9.client.mvvm.view.fragment.ProfileFragment
import kr.co.express9.client.mvvm.view.fragment.SearchFragment
import kr.co.express9.client.mvvm.viewModel.MainViewModel
import kr.co.express9.client.mvvm.viewModel.SuggestionViewModel
import kr.co.express9.client.util.extension.launchActivity
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val mainViewModel: MainViewModel by viewModel()
    private val suggestionViewModel: SuggestionViewModel by viewModel()

    private val homeFragment: HomeFragment by inject()
    private val searchFragment: SearchFragment by inject()
    private val martFragment: MartFragment by inject()
    private val profileFragment: ProfileFragment by inject()

    private var activeFragment: Fragment = homeFragment
    private lateinit var selectedFragment: Fragment
    private lateinit var toolbarMenu: Menu
    private lateinit var searchMenu: MenuItem
    private lateinit var alarmMenu: MenuItem
    private lateinit var cartMenu: MenuItem
    private lateinit var searchView: SearchView
    private var toolbarState = ToolbarState.MENU_IS_NOT_CREATED

    enum class ToolbarState {
        MENU_IS_NOT_CREATED,
        MENU_IS_CREATED
    }

    override fun initStartView(isRestart: Boolean) {
        // init fragment
        supportFragmentManager.beginTransaction().add(R.id.frameLayout, homeFragment).hide(homeFragment).commit()
        supportFragmentManager.beginTransaction().add(R.id.frameLayout, searchFragment).hide(searchFragment).commit()
        supportFragmentManager.beginTransaction().add(R.id.frameLayout, martFragment).hide(martFragment).commit()
        supportFragmentManager.beginTransaction().add(R.id.frameLayout, profileFragment).hide(profileFragment).commit()

        // BottomNavigation 클릭에 따른 Toolbar UI 변경
        dataBinding.bottomNavigation.setOnNavigationItemSelectedListener {
            setFragment(it.itemId)
            return@setOnNavigationItemSelectedListener true
        }

        mainViewModel.selectedBottomNavigationItemId.observe(this, Observer { selectedItemId ->
            setFragment(selectedItemId)
        })

        // action bar 등록
        setSupportActionBar(dataBinding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.cart -> launchActivity<CartActivity>()
            R.id.alarm -> launchActivity<NotificationActivity>()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (toolbarState == ToolbarState.MENU_IS_NOT_CREATED) {
            toolbarState = ToolbarState.MENU_IS_CREATED
            toolbarMenu = menu
        }
        menuInflater.inflate(R.menu.menu_search, toolbarMenu)
        menuInflater.inflate(R.menu.menu_cart, toolbarMenu)
        menuInflater.inflate(R.menu.menu_alarm, toolbarMenu)

        cartMenu = dataBinding.toolbar.menu.findItem(R.id.cart)
        alarmMenu = dataBinding.toolbar.menu.findItem(R.id.alarm)
        searchMenu = dataBinding.toolbar.menu.findItem(R.id.search)

        dataBinding.toolbar.menu.findItem(R.id.alarm).isVisible = true
        dataBinding.toolbar.menu.findItem(R.id.alarm).isVisible = false
        dataBinding.toolbar.menu.findItem(R.id.search).isVisible = false

        val adapter = SimpleCursorAdapter(
            this,
            android.R.layout.simple_list_item_1,
            null,
            arrayOf(SearchManager.SUGGEST_COLUMN_TEXT_1),
            intArrayOf(android.R.id.text1),
            0
        )

        searchView = searchMenu.actionView as SearchView
        searchView.queryHint = getString(R.string.menu_search_hint)
        searchView.suggestionsAdapter = adapter
        val editText = searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
        editText.setTextColor(ContextCompat.getColor(this, R.color.white))
        editText.setHintTextColor(ContextCompat.getColor(this, R.color.white_50))

        // searchView icon tint (style의 colorControlNormal로 대체)
        // searchView.findViewById<ImageView>(androidx.appcompat.R.productSeq.search_button)
        //     .setImageResource(R.drawable.ic_search_24dp)

        // nexus api28에서 title이 보이는 이슈 해결
        searchView.setOnCloseListener {
            dataBinding.tvTitle.visibility = View.VISIBLE
            suggestionViewModel.putSuggestion(null)
            return@setOnCloseListener false
        }

        searchView.setOnSearchClickListener {
            dataBinding.tvTitle.visibility = View.GONE
        }

        // 입력 및 검색 listener
        Observable.create(ObservableOnSubscribe<String> { subscriber ->
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextChange(newText: String?): Boolean {
                    subscriber.onNext(newText!!)
                    return false
                }

                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (query != null) suggestionViewModel.putSuggestion(query)
                    return false
                }
            })
        }).map { it.toLowerCase().trim() }
            .debounce(300, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .filter { it.isNotBlank() && it.length >= 2 }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { adapter.swapCursor(suggestionViewModel.getSuggestionCursor(it)) }
            .apply { addDisposable(this) }

        // 제안된 검색어 선택 listener
        searchView.setOnSuggestionListener(object : SearchView.OnSuggestionListener {
            override fun onSuggestionSelect(i: Int): Boolean {
                return false
            }

            override fun onSuggestionClick(index: Int): Boolean {
                val suggestion = suggestionViewModel.selectSuggestion(index)
                searchView.setQuery(suggestion, true)
                return true
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    /**
     * 다른 Fragment에서 화면 이동시 사용
     */
    fun setBottomNavigation(selectedItemId: Int) {
        dataBinding.bottomNavigation.selectedItemId = selectedItemId
    }

    /**
     * 화면 변경
     */
    private fun setFragment(selectedItemId: Int) {
        selectedFragment = if (toolbarState == ToolbarState.MENU_IS_CREATED) {
            if (!cartMenu.isVisible) cartMenu.isVisible = true
            if (alarmMenu.isVisible) alarmMenu.isVisible = false
            if (searchMenu.isVisible) searchMenu.isVisible = false
            if (!searchView.isIconified) searchView.onActionViewCollapsed()
            dataBinding.tvTitle.visibility = View.VISIBLE
            dataBinding.ivMagarine.visibility = View.GONE

            dataBinding.tvTitle.setTextColor(Color.parseColor("#ffffff"))
            val fragment = when (selectedItemId) {
                R.id.bn_home -> {
                    dataBinding.ivMagarine.visibility = View.VISIBLE
                    dataBinding.tvTitle.visibility = View.GONE
                    homeFragment
                }
                R.id.bn_search -> {
                    dataBinding.tvTitle.text = getString(R.string.menu_search)
                    searchMenu.isVisible = true
                    searchFragment
                }
                R.id.bn_market -> {
                    dataBinding.tvTitle.text = getString(R.string.menu_mart)
                    martFragment
                }
                else -> {
                    dataBinding.tvTitle.text = getString(R.string.menu_profile)
                    cartMenu.isVisible = false
                    alarmMenu.isVisible = true
                    profileFragment
                }
            }

            fragment
        } else homeFragment

        supportFragmentManager.beginTransaction().hide(activeFragment).show(selectedFragment).commit()
        activeFragment = selectedFragment
        mainViewModel.setSelectedBottomNavigationItemId(selectedItemId)
    }
}