package kr.co.express9.client.mvvm.viewModel

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kr.co.express9.client.base.BaseViewModel

class MapViewModel : BaseViewModel() {

    enum class EVENT {
        CLICK_MARKER
    }

    private val _location = MutableLiveData<Location>()
    val location: LiveData<Location>
        get() = _location

    private val _event = MutableLiveData<EVENT>()
    val event: LiveData<EVENT>
        get() = _event

}
