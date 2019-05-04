package kr.co.express9.client.mvvm.view

import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import kr.co.express9.client.R
import kr.co.express9.client.adapter.CartAdapter
import kr.co.express9.client.base.BaseActivity
import kr.co.express9.client.databinding.ActivityCartBinding
import kr.co.express9.client.mvvm.viewModel.CartViewModel
import kr.co.express9.client.util.extension.toast
import org.koin.androidx.viewmodel.ext.android.viewModel

class CartActivity : BaseActivity<ActivityCartBinding>(R.layout.activity_cart) {

    private val cartViewModel: CartViewModel by viewModel()

    override fun initStartView() {
        val cartAdapter = CartAdapter()
        dataBinding.cartAdapter = cartAdapter

        cartViewModel.cartGoods.observe(this, Observer {
            it.forEachIndexed { i, cartGoods ->
                if (cartGoods.goods.market !in cartAdapter.marketList) {
                    cartGoods.isHeader = true
                    cartAdapter.marketList.add(cartGoods.goods.market)
                    cartAdapter.headerIdx.add(i)
                }
            }
            cartAdapter.goodsList = it
            cartAdapter.notifyDataSetChanged()
        })

        cartViewModel.getGoods()

        // action bar 등록
        setSupportActionBar(dataBinding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
            R.id.delete -> toast("삭제")
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_delete, menu)

        return super.onCreateOptionsMenu(menu)
    }
}