package kr.co.express9.client.mvvm.viewModel

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.LatLng
import kr.co.express9.client.base.BaseViewModel
import kr.co.express9.client.mvvm.model.KakaoRepository
import kr.co.express9.client.mvvm.model.MapRepository
import org.koin.standalone.inject

class MapViewModel : BaseViewModel<MapViewModel.Event>() {

    private val mapRepository: MapRepository by inject()

    enum class Event {
        CLICK_MARKER
    }

    private val _location = MutableLiveData<Location>()
    val location: LiveData<Location>
        get() = _location

    fun getMartSearch(northEast: LatLng, southWest:LatLng){



    }

}