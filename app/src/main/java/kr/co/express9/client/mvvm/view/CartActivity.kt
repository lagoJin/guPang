package kr.co.express9.client.mvvm.view

import android.view.Menu
import android.view.MenuItem
import kr.co.express9.client.R
import kr.co.express9.client.base.BaseActivity
import kr.co.express9.client.databinding.ActivityCartBinding
import kr.co.express9.client.util.extension.toast

class CartActivity : BaseActivity<ActivityCartBinding>(R.layout.activity_cart) {

    override fun initStartView() {

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