package kr.co.express9.client.mvvm.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import kr.co.express9.client.base.BaseViewModel
import kr.co.express9.client.mvvm.model.MapRepository
import kr.co.express9.client.mvvm.model.data.Mart
import kr.co.express9.client.mvvm.model.enumData.StatusEnum
import kr.co.express9.client.util.extension.networkError
import org.koin.standalone.inject

class MapViewModel : BaseViewModel<MapViewModel.Event>() {

    private val mapRepository: MapRepository by inject()

    enum class Event {
        MART_LIST,
        CLICK_MARKER
    }

    private val _marts = MutableLiveData<List<Mart>>()
    val marts: LiveData<List<Mart>>
        get() = _marts

    fun getMartSearch(northEast: LatLng, southWest: LatLng) {
        mapRepository.mapMartList(southWest.latitude, northEast.latitude, southWest.longitude, northEast.longitude)
                .subscribe(
                        {
                            if (it.status == StatusEnum.SUCCESS) {
                                _marts.value = it.result as List<Mart>
                                _event.value = Event.MART_LIST
                            }
                        },
                        { throwable -> networkError(throwable) }
                ).apply { addDisposable(this) }

    }

}