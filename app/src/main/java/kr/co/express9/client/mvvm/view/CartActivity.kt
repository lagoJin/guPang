package kr.co.express9.client.mvvm.view

import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.Observer
import kr.co.express9.client.R
import kr.co.express9.client.adapter.CartAdapter
import kr.co.express9.client.base.BaseActivity
import kr.co.express9.client.databinding.ActivityCartBinding
import kr.co.express9.client.mvvm.viewModel.CartViewModel
import kr.co.express9.client.util.Logger
import kr.co.express9.client.util.extension.toast
import org.koin.androidx.viewmodel.ext.android.viewModel

class CartActivity : BaseActivity<ActivityCartBinding>(R.layout.activity_cart) {
    private val cartViewModel: CartViewModel by viewModel()

    private lateinit var cartAdapter: CartAdapter
    private val onSelect: (Int) -> Unit = { selectIdx ->
        cartViewModel.selectGoods(selectIdx) { idx ->
            cartAdapter.notifyItemChanged(idx, "onSelect")
        }
    }

    private val onExpand: (Int) -> Unit = { expandIdx ->
        cartViewModel.expandGoods(expandIdx) { startIdx, endIdx ->
            cartAdapter.notifyItemRangeChanged(startIdx, endIdx, "onExpand")
        }
    }

    private val onChangeAmount: (Int, Boolean) -> Unit = { idx, isPlus ->
        cartViewModel.changeAmount(idx, isPlus) { cartAdapter.notifyItemChanged(it, "onChangeAmount") }
    }

    override fun initStartView(isRestart: Boolean) {
        cartAdapter = CartAdapter(onSelect, onExpand, onChangeAmount)

        dataBinding.cartAdapter = cartAdapter

        cartViewModel.cartGoods.observe(this, Observer {
            Logger.d("cartGoods Observe $it")
            cartAdapter.goodsList = it
            cartAdapter.notifyDataSetChanged()
        })

        // action bar 등록
        setSupportActionBar(dataBinding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        if (isRestart) cartViewModel.getGoods()
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