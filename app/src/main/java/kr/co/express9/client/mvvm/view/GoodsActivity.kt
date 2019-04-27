package kr.co.express9.client.mvvm.view

import kr.co.express9.client.R
import kr.co.express9.client.base.BaseActivity
import kr.co.express9.client.databinding.ActivityGoodsBinding
import kr.co.express9.client.mvvm.viewModel.CategoryGoodsViewModel

class GoodsActivity : BaseActivity<ActivityGoodsBinding>(R.layout.activity_goods) {

    override fun initStartView() {
        val goods = intent.getSerializableExtra("goods") as CategoryGoodsViewModel.GoodsDummy
        dataBinding.tvGoodsName.text = goods.name
    }

}