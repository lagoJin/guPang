package kr.co.express9.client.mvvm.view

import android.view.Menu
import android.view.MenuItem
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
        val product = intent.getSerializableExtra("product") as Product
        productViewModel.setProduct(product)
        dataBinding.productViewModel = productViewModel
        dataBinding.bAddToCart.setOnClickListener {
            if (productViewModel.itemNum.value == 0) toast(R.string.choose_number_of_product)
            else {
                // 장보기 메모에 상품 추가 예정
                showCheckCartAlert()
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
        return super.onCreateOptionsMenu(menu)
    }

    /**
     * 장보기 메모담기 확인
     */
    private fun showCheckCartAlert() {
        dialog<AlertCheckCartBinding>(R.layout.alert_check_cart) { dialog, binding ->
            binding.bNo.setOnClickListener { dialog.dismiss() }
            binding.bYes.setOnClickListener {
                launchActivity<CartActivity>()
                productViewModel.resetItem()
                dialog.dismiss()
            }
            dialog.show()
        }
    }

}