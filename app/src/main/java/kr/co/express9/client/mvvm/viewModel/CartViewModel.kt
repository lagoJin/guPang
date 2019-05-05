package kr.co.express9.client.mvvm.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kr.co.express9.client.base.BaseViewModel
import kr.co.express9.client.mvvm.model.data.CartGoodsDummy
import kr.co.express9.client.mvvm.model.data.GoodsDummy
import kr.co.express9.client.util.Logger

class CartViewModel : BaseViewModel<CartViewModel.Event>() {

    enum class Event {
        SELECTED,
        NOT_SELECTED
    }

    private val _cartGoods = MutableLiveData<ArrayList<CartGoodsDummy>>()
    val cartGoods: LiveData<ArrayList<CartGoodsDummy>>
        get() = _cartGoods

    private val _totalPrice = MutableLiveData<Int>().apply { value = 0 }
    val totalPrice: LiveData<Int>
        get() = _totalPrice

    private val _totalSalePrice = MutableLiveData<Int>().apply { value = 0 }
    val totalSalePrice: LiveData<Int>
        get() = _totalSalePrice

    private val _totalPayment = MutableLiveData<Int>().apply { value = 0 }
    val totalPayment: LiveData<Int>
        get() = _totalPayment

    private var _selectedList = MutableLiveData<ArrayList<Int>>().apply { value = ArrayList() }
    val selectedList: LiveData<ArrayList<Int>>
        get() = _selectedList

    private val marketList = ArrayList<String>()
    private val headerIdx = ArrayList<Int>()

    private fun calculatePrice(isPlus: Boolean, price: Int, salePrice: Int) {
        if (isPlus) {
            _totalPrice.value = _totalPrice.value!! + price
            _totalSalePrice.value = _totalSalePrice.value!! + salePrice
        } else {
            _totalPrice.value = _totalPrice.value!! - price
            _totalSalePrice.value = _totalSalePrice.value!! - salePrice
        }
        _totalPayment.value = _totalPrice.value!! - _totalSalePrice.value!!
    }

    fun selectGoods(idx: Int, cb: (index: Int) -> Unit) {
        // 상태변경
        val isSelected = !_cartGoods.value!![idx].isSelected
        _cartGoods.value!![idx].isSelected = isSelected

        // 선택목록에 갱신
        if (isSelected) _selectedList.value!!.add(idx)
        else _selectedList.value!!.remove(idx)

        // 이벤트 전달
        _event.value = if (selectedList.value!!.size > 0) Event.SELECTED
        else Event.NOT_SELECTED
        Logger.d("size!! ${selectedList.value!!.size}")

        // 금액변경
        val cart = _cartGoods.value!![idx]
        val price = cart.goods.price * cart.total
        val salePrice = (cart.goods.price - cart.goods.salePrice) * cart.total
        calculatePrice(isSelected, price, salePrice)

        cb(idx)
    }

    fun expandGoods(idx: Int, cb: (startIdx: Int, endIdx: Int) -> Unit) {
        _cartGoods.value!![idx].isExpanded

        val marketIdx = marketList.indexOf(_cartGoods.value!![idx].goods.market)
        val endIdx = if (headerIdx.size == marketIdx + 1) _cartGoods.value!!.size - 1 else headerIdx[marketIdx + 1] - 1

        val toExpanded = !_cartGoods.value!![idx].isExpanded
        for (index in idx..endIdx) {
            _cartGoods.value!![index].isExpanded = toExpanded
        }
        cb(idx, endIdx + 1)
    }

    fun changeAmount(idx: Int, isPlus: Boolean, cb: (idx: Int) -> Unit) {
        val total = _cartGoods.value!![idx].total
        _cartGoods.value!![idx].total = when {
            isPlus -> total + 1
            total > 1 -> total - 1
            else -> 1
        }

        // 해당 상품이 선택되어 있는 경우 금액 변경
        val cart = _cartGoods.value!![idx]
        if (cart.isSelected) {
            val price = cart.goods.price
            val salePrice = cart.goods.price - cart.goods.salePrice
            calculatePrice(isPlus, price, salePrice)
        }
        cb(idx)
    }

    fun getGoods() {
        val dummyCartGoods = ArrayList<CartGoodsDummy>()
        dummyCartGoods.add(
            CartGoodsDummy(
                GoodsDummy(
                    0,
                    "https://file.mk.co.kr/meet/neds/2015/04/image_readtop_2015_312234_14279358191849603.jpg",
                    "사과",
                    7000,
                    3000,
                    "메로나 마트",
                    "우리집 꼬꼬가 낳은 사과"
                )
                , 1
            )
        )
        dummyCartGoods.add(
            CartGoodsDummy(
                GoodsDummy(
                    1,
                    "http://static.hubzum.zumst.com/hubzum/2017/11/06/13/b9a069e2cb1c4eaf9cfd70071f009cea_780x0c.jpg",
                    "배",
                    7000,
                    3000,
                    "메로나 마트",
                    "우리집 꼬꼬가 낳은 배"
                )
                , 1
            )
        )
        dummyCartGoods.add(
            CartGoodsDummy(
                GoodsDummy(
                    2,
                    "http://gdimg.gmarket.co.kr/701320448/still/600?ver=1546566195",
                    "계란",
                    7000,
                    3000,
                    "메로나 마트",
                    "우리집 꼬꼬가 낳은 계란"
                )
                , 1
            )
        )
        dummyCartGoods.add(
            CartGoodsDummy(
                GoodsDummy(
                    3,
                    "http://www.mega-mart.co.kr/medias/sys_master/images/images/hdb/hd6/9093541396510/05666966-R-400Wx400H.jpg",
                    "왕란",
                    7000,
                    3000,
                    "메로나 마트",
                    "우리집 꼬꼬가 낳은 왕란"
                )
                , 1
            )
        )
        dummyCartGoods.add(
            CartGoodsDummy(
                GoodsDummy(
                    4,
                    "http://www.fsnews.co.kr/news/photo/201807/30092_25384_5554.jpg",
                    "오곡",
                    7000,
                    3000,
                    "메로나 마트",
                    "우리집 꼬꼬가 낳은 오곡"
                )
                , 1
            )
        )
        dummyCartGoods.add(
            CartGoodsDummy(
                GoodsDummy(
                    5,
                    "https://dimg.donga.com/egc/CDB/WOMAN/Article/14/65/46/16/1465461617806.jpg",
                    "현미",
                    7000,
                    3000,
                    "장보고 마트",
                    "우리집 꼬꼬가 낳은 현미"
                )
                , 1
            )
        )
        dummyCartGoods.add(
            CartGoodsDummy(
                GoodsDummy(
                    6,
                    "http://img.danawa.com/prod_img/500000/729/980/img/1980729_1.jpg?shrink=500:500&_v=20180726121338",
                    "오렌지주스",
                    7000,
                    3000,
                    "장보고 마트",
                    "우리집 꼬꼬가 낳은 오렌지주스"
                )
                , 1
            )
        )
        dummyCartGoods.add(
            CartGoodsDummy(
                GoodsDummy(
                    7,
                    "http://www.hoonipizza.co.kr/wp-content/uploads/2018/07/cider1250.png",
                    "사이다",
                    7000,
                    3000,
                    "장보고 마트",
                    "우리집 꼬꼬가 낳은 사이다"
                )
                , 1
            )
        )
        dummyCartGoods.add(
            CartGoodsDummy(
                GoodsDummy(
                    8,
                    "https://www.costco.co.kr/medias/sys_master/images/he9/h9a/9867844190238.jpg",
                    "신라면",
                    7000,
                    3000,
                    "장보고 마트",
                    "우리집 꼬꼬가 낳은 신라면"
                )
                , 1
            )
        )
        dummyCartGoods.add(
            CartGoodsDummy(
                GoodsDummy(
                    9,
                    "http://image.auction.co.kr/itemimage/13/75/dd/1375ddf586.jpg",
                    "만두",
                    7000,
                    3000,
                    "장보고 마트",
                    "우리집 꼬꼬가 낳은 만두"
                )
                , 1
            )
        )
        dummyCartGoods.add(
            CartGoodsDummy(
                GoodsDummy(
                    10,
                    "https://www.twosome.co.kr:7009/Twosome_file/PRODUCT/2035_big_img",
                    "아이스크림",
                    7000,
                    3000,
                    "장보고 마트",
                    "우리집 꼬꼬가 낳은 아이스크림"
                )
                , 1
            )
        )
        dummyCartGoods.add(
            CartGoodsDummy(
                GoodsDummy(
                    11,
                    "http://img.danawa.com/prod_img/500000/735/833/img/3833735_1.jpg?shrink=500:500&_v=20170331144734",
                    "우유",
                    7000,
                    3000,
                    "바나나 마트",
                    "우리집 꼬꼬가 낳은 우유"
                )
                , 1
            )
        )
        dummyCartGoods.add(
            CartGoodsDummy(
                GoodsDummy(
                    12,
                    "http://photo3.enuri.com/data/images/service/middle/1830000/1830429.jpg",
                    "치약",
                    7000,
                    3000,
                    "바나나 마트",
                    "우리집 꼬꼬가 낳은 치약"
                )
                , 1
            )
        )
        dummyCartGoods.add(
            CartGoodsDummy(
                GoodsDummy(
                    13,
                    "https://smartbuygroup.wisacdn.com/_data/product/201807/25/d67e8dbfcede136c7386eaabe6c8d4df.png",
                    "화장지",
                    7000,
                    3000,
                    "바나나 마트",
                    "우리집 꼬꼬가 낳은 화장지"
                )
                , 1
            )
        )
        dummyCartGoods.add(
            CartGoodsDummy(
                GoodsDummy(
                    14,
                    "https://post-phinf.pstatic.net/MjAxODAxMjJfMTU4/MDAxNTE2NTk5MzE0Mzk3.tpo98J5uKOWXI_DKeVshDaXv0A-6fpTvDdbDWX3Uqbkg.dJcyQ8Y4rABIDFt2kbJRsUVjgLmT0Mo9hYVurPAgyfIg.JPEG/6-1.jpg?type=w1200",
                    "강아지",
                    7000,
                    3000,
                    "바나나 마트",
                    "우리집 꼬꼬가 낳은 강아지"
                )
                , 1
            )
        )
        dummyCartGoods.add(
            CartGoodsDummy(
                GoodsDummy(
                    15,
                    "http://mblogthumb1.phinf.naver.net/MjAxODA0MjZfMjMz/MDAxNTI0NzIxMjA5MTk5.cY5q4PgtCDnwjQNRWoVCfUa0m8NMDP8toQuSFVR-lwEg.yuufdLHiwiWekRlGOce-0FUGdpgA0ic1U7tXz2Bo5xgg.PNG.thecontest/%EA%B3%A0%EC%96%91%EC%9D%B48.png?type=w800",
                    "고양이",
                    7000,
                    3000,
                    "바나나 마트",
                    "우리집 꼬꼬가 낳은 고양이"
                )
                , 1
            )
        )

        dummyCartGoods.add(
            CartGoodsDummy(
                GoodsDummy(
                    0,
                    "https://file.mk.co.kr/meet/neds/2015/04/image_readtop_2015_312234_14279358191849603.jpg",
                    "사과",
                    7000,
                    3000,
                    "바나나 마트",
                    "우리집 꼬꼬가 낳은 사과"
                )
                , 1
            )
        )

        marketList.clear()
        headerIdx.clear()
        dummyCartGoods.forEachIndexed { i, cartGoods ->
            if (cartGoods.goods.market !in marketList) {
                cartGoods.isHeader = true
                marketList.add(cartGoods.goods.market)
                headerIdx.add(i)
            }
        }
        _cartGoods.value = dummyCartGoods
    }
}