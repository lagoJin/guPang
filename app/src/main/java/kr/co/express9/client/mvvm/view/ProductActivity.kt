package kr.co.express9.client.mvvm.view

import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.lifecycle.Observer
import kr.co.express9.client.R
import kr.co.express9.client.base.BaseActivity
import kr.co.express9.client.databinding.ActivityProductBinding
import kr.co.express9.client.databinding.AlertCheckCartBinding
import kr.co.express9.client.mvvm.model.data.Product
import kr.co.express9.client.mvvm.viewModel.ProductViewModel
import kr.co.express9.client.util.extension.dialog
import kr.co.express9.client.util.extension.launchActivity
import kr.co.express9.client.util.extension.toast
import org.koin.androidx.viewmodel.ext.android.viewModel


class ProductActivity : BaseActivity<ActivityProductBinding>(R.layout.activity_product) {

    private val productViewModel: ProductViewModel by viewModel()

    override fun initStartView(isRestart: Boolean) {
        dataBinding.lifecycleOwner = this
        val productIntent = intent.getSerializableExtra("product")
        val productSeq = intent.getSerializableExtra("productSeq")
        if (productIntent != null) {
            productViewModel.setProduct(productIntent as Product)
        } else if (productSeq != null) {
            productViewModel.getProduct(productSeq as Int) {
                productViewModel.setProduct(it)
            }
        }

        dataBinding.productViewModel = productViewModel
        dataBinding.bAddToCart.setOnClickListener {
            if (productViewModel.itemNum.value == 0) toast(R.string.choose_number_of_product)
            else {
                // 장보기 메모에 상품 추가 예정
                productViewModel.addCartProduct {
                    if (it) showCheckCartAlert()
                    else toast(R.string.faile_add_product_to_cart)
                }
            }
        }

        // productViewModel
        productViewModel.countDown.observe(this, Observer {
            dataBinding.ttvTimer.setEndTime(it)
        })

        // action bar 등록
        setSupportActionBar(dataBinding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.cart -> launchActivity<CartActivity>()
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_cart, menu)

        // set menu tint
        var drawable = menu.findItem(R.id.cart).icon
        drawable = DrawableCompat.wrap(drawable)
        DrawableCompat.setTint(drawable, ContextCompat.getColor(this, R.color.white))
        menu.findItem(R.id.cart).icon = drawable

        return super.onCreateOptionsMenu(menu)
    }

    /**
     * 장보기 메모담기 확인
     */
    private fun showCheckCartAlert() {
        productViewModel.resetItem()
        dialog<AlertCheckCartBinding>(R.layout.alert_check_cart) { dialog, binding ->
            binding.bNo.setOnClickListener { dialog.dismiss() }
            binding.bYes.setOnClickListener {
                launchActivity<CartActivity>()
                dialog.dismiss()
            }
            dialog.show()
        }
    }

}