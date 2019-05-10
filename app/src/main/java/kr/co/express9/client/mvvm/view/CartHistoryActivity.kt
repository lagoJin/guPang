package kr.co.express9.client.mvvm.view

import android.view.MenuItem
import androidx.lifecycle.Observer
import kr.co.express9.client.R
import kr.co.express9.client.adapter.CartHistoryAdapter
import kr.co.express9.client.base.BaseActivity
import kr.co.express9.client.databinding.ActivityCartHistoryBinding
import kr.co.express9.client.mvvm.viewModel.CartHistoryViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class CartHistoryActivity : BaseActivity<ActivityCartHistoryBinding>(R.layout.activity_cart_history) {

    private val cartHistoryViewModel: CartHistoryViewModel by viewModel()
    private lateinit var cartHistoryAdapter: CartHistoryAdapter

    override fun initStartView(isRestart: Boolean) {
        cartHistoryAdapter = CartHistoryAdapter()
        dataBinding.cartHistoryAdapter = cartHistoryAdapter
        dataBinding.cartHistoryViewModel = cartHistoryViewModel

        cartHistoryViewModel.cartHistory.observe(this, Observer {
            cartHistoryAdapter.cartHistory = it
            cartHistoryAdapter.notifyDataSetChanged()
        })
        // action bar 등록
        setSupportActionBar(dataBinding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (!isRestart) cartHistoryViewModel.getHistoryByMonth()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}