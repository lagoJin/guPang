package kr.co.express9.client.mvvm.model

import kr.co.express9.client.mvvm.model.remote.MapRemoteDataSource
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class MapRepository(
    private val mapRemoteDataSource: MapRemoteDataSource
) {


}