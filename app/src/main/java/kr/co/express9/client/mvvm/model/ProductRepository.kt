package kr.co.express9.client.mvvm.model

import kr.co.express9.client.mvvm.model.api.ProductAPI
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class ProductRepository : KoinComponent {

    private val productAPI: ProductAPI by inject()


}