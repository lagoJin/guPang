package kr.co.express9.client.mvvm.view

import android.view.Menu
import android.view.MenuItem
import kr.co.express9.client.R
import kr.co.express9.client.base.BaseActivity
import kr.co.express9.client.databinding.ActivityGoodsBinding
import kr.co.express9.client.mvvm.viewModel.CategoryGoodsViewModel
import kr.co.express9.client.util.extension.launchActivity

class GoodsActivity : BaseActivity<ActivityGoodsBinding>(R.layout.activity_goods) {

    override fun initStartView() {
        dataBinding.goods = intent.getSerializableExtra("goods") as CategoryGoodsViewModel.GoodsDummy

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

}