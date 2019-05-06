package kr.co.express9.client.mvvm.model

import io.reactivex.Single
import kr.co.express9.client.mvvm.model.api.ProductAPI
import kr.co.express9.client.mvvm.model.data.Product
import kr.co.express9.client.mvvm.model.data.Response
import kr.co.express9.client.mvvm.model.data.User
import kr.co.express9.client.util.extension.networkCommunication
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class ProductRepository : KoinComponent {

    private val productAPI: ProductAPI by inject()

    /**
     * Remote
     */
    fun getProducts(): Single<Response<ArrayList<Product>>> {
        return productAPI.getProducts().networkCommunication()
    }

    fun getProduct(productSeq: String): Single<Response<Product>> {
        return productAPI.getProduct(productSeq).networkCommunication()
    }

    fun searchProducts(category: String, name: String): Single<Response<ArrayList<Product>>> {
        return productAPI.searchProducts(category, name).networkCommunication()
    }

}