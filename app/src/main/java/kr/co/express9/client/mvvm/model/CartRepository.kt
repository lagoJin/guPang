package kr.co.express9.client.mvvm.model

import io.reactivex.Single
import kr.co.express9.client.mvvm.model.api.CartAPI
import kr.co.express9.client.mvvm.model.data.CartHistory
import kr.co.express9.client.mvvm.model.data.CartProduct
import kr.co.express9.client.mvvm.model.data.Response
import kr.co.express9.client.util.extension.networkCommunication
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class CartRepository : KoinComponent {

    private val cartAPI: CartAPI by inject()

    fun addCartProduct(count: Int, productSeq: Int): Single<Response<Unit>> {
        return cartAPI.addCartProduct(count, productSeq).networkCommunication()
    }

    fun changeAmount(count: Int, productSeq: Int): Single<Response<Unit>> {
        return cartAPI.changeAmount(count, productSeq).networkCommunication()
    }

    fun getCartProducts(): Single<Response<ArrayList<CartProduct>>> {
        return cartAPI.getCartProducts().networkCommunication()
    }

    fun deleteCartProduct(productSeq: Int): Single<Response<Unit>> {
        return cartAPI.deleteCartProduct(productSeq).networkCommunication()
    }

    fun getHistoryByMonth(purchaseYm: String): Single<Response<ArrayList<CartHistory>>> {
        return cartAPI.getHistoryByMonth(purchaseYm).networkCommunication()
    }

    fun purchaseCartProduct(count: Int, productSeq: Int): Single<Response<Unit>> {
        return cartAPI.purchaseCartProduct(count, productSeq).networkCommunication()
    }
}