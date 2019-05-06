package kr.co.express9.client.mvvm.model

import io.reactivex.Single
import kr.co.express9.client.mvvm.model.api.MartAPI
import kr.co.express9.client.mvvm.model.data.Mart
import kr.co.express9.client.mvvm.model.data.Response
import kr.co.express9.client.mvvm.model.data.ResultNeedModify
import kr.co.express9.client.util.extension.netWorkSubscribe

class MapRepository(private val martAPI: MartAPI) {

    /*fun searchMartList(): Single<List<Mart>> {

    }*/

    fun mapMartList(xx: Double, xy: Double, yx: Double, yy: Double): Single<Response<List<Mart>>> {
        return martAPI.getMarts(xx, xy, yx, yy).netWorkSubscribe()
    }

}