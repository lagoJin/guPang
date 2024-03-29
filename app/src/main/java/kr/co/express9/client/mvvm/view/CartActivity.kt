package kr.co.express9.client.mvvm.view

import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.Observer
import kr.co.express9.client.R
import kr.co.express9.client.adapter.CartAdapter
import kr.co.express9.client.adapter.CartAdapter.Payload
import kr.co.express9.client.base.BaseActivity
import kr.co.express9.client.databinding.ActivityCartBinding
import kr.co.express9.client.mvvm.viewModel.CartViewModel
import kr.co.express9.client.util.extension.toast
import org.koin.androidx.viewmodel.ext.android.viewModel

class CartActivity : BaseActivity<ActivityCartBinding>(R.layout.activity_cart) {

    private val cartViewModel: CartViewModel by viewModel()

    private lateinit var cartAdapter: CartAdapter
    private val onSelect: (Int) -> Unit = { selectIdx ->
        cartViewModel.selectGoods(selectIdx) { idx ->
            cartAdapter.notifyItemChanged(idx, Payload.UPDATE_CART_PRODUCT)
        }
    }

    private val onExpand: (Int) -> Unit = { expandIdx ->
        cartViewModel.expandGoods(expandIdx) { startIdx, endIdx ->
            cartAdapter.notifyItemRangeChanged(startIdx, endIdx, Payload.UPDATE_CART_PRODUCT)
        }
    }

    private val onChangeAmount: (Int, Boolean) -> Unit = { idx, isPlus ->
        cartViewModel.changeAmount(idx, isPlus) { cartAdapter.notifyItemChanged(it, Payload.UPDATE_CART_PRODUCT) }
    }

    override fun initStartView(isRestart: Boolean) {
        cartAdapter = CartAdapter(onSelect, onExpand, onChangeAmount)

        dataBinding.cartAdapter = cartAdapter
        dataBinding.cartViewModel = cartViewModel
        dataBinding.bGoToShopping.setOnClickListener { finish() }
        dataBinding.bDelete.setOnClickListener {
            cartViewModel.deleteCartProduct {
                toast(R.string.delete_complete)
                dataBinding.clCalculator.visibility = View.GONE
            }
        }
        dataBinding.bPurchaseComplete.setOnClickListener {
            cartViewModel.purchaseCartProduct {
                toast(R.string.purchase_complete)
                dataBinding.clCalculator.visibility = View.GONE
            }
        }

        cartViewModel.event.observe(this, Observer { event ->
            when (event) {
                CartViewModel.Event.SELECTED -> dataBinding.clCalculator.visibility = View.VISIBLE
                CartViewModel.Event.NOT_SELECTED -> dataBinding.clCalculator.visibility = View.GONE
            }
        })

        cartViewModel.cartProducts.observe(this, Observer {
            cartAdapter.cartProducts = it
            cartAdapter.notifyDataSetChanged()
        })

        // action bar 등록
        setSupportActionBar(dataBinding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (!isRestart) cartViewModel.getCartProducts()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}