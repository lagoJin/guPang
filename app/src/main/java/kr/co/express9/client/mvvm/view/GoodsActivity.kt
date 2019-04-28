package kr.co.express9.client.mvvm.view

import android.app.Dialog
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import kr.co.express9.client.R
import kr.co.express9.client.base.BaseActivity
import kr.co.express9.client.databinding.ActivityGoodsBinding
import kr.co.express9.client.databinding.AlertCheckCartBinding
import kr.co.express9.client.mvvm.viewModel.CategoryGoodsViewModel
import kr.co.express9.client.mvvm.viewModel.GoodsViewModel
import kr.co.express9.client.util.extension.launchActivity
import kr.co.express9.client.util.extension.toast
import org.koin.androidx.viewmodel.ext.android.viewModel

class GoodsActivity : BaseActivity<ActivityGoodsBinding>(R.layout.activity_goods) {

    private val goodsViewModel: GoodsViewModel by viewModel()

    override fun initStartView() {
        val goods = intent.getSerializableExtra("goods") as CategoryGoodsViewModel.GoodsDummy
        goodsViewModel.setGoods(goods)
        dataBinding.goodsViewModel = goodsViewModel
        goodsViewModel.itemNum.observe(this, Observer {
            dataBinding.tvNumber.text = it.toString()
        })

        dataBinding.bAddToCart.setOnClickListener {
            if (goodsViewModel.itemNum.value == 0) toast(R.string.choose_number_of_goods)
            else showCheckCartAlert()
        }

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
        val binding = DataBindingUtil.inflate<AlertCheckCartBinding>(
            LayoutInflater.from(this),
            R.layout.alert_check_cart,
            null,
            false
        )

        val dialog = Dialog(this, R.style.CustomDialog)
        dialog.setContentView(binding.root)
        dialog.setCanceledOnTouchOutside(true)
        dialog.show()

        binding.bNo.setOnClickListener { dialog.dismiss() }
        binding.bYes.setOnClickListener {
            launchActivity<CartActivity>()
            goodsViewModel.resetItem()
            dialog.dismiss()
        }
    }

}