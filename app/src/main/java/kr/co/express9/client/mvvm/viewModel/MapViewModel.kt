package kr.co.express9.client.mvvm.viewModel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.orhanobut.hawk.Hawk
import kr.co.express9.client.base.BaseViewModel
import kr.co.express9.client.mvvm.model.MartRepository
import kr.co.express9.client.mvvm.model.data.Mart
import kr.co.express9.client.mvvm.model.data.User
import kr.co.express9.client.mvvm.model.enumData.StatusEnum
import kr.co.express9.client.mvvm.model.preference.MartPreferenceDataSource
import kr.co.express9.client.util.Logger
import kr.co.express9.client.util.extension.networkError
import org.koin.standalone.inject

class MapViewModel : BaseViewModel<MapViewModel.Event>() {

    private val marketRepository: MartRepository by inject()

    enum class Event {
        MART_LIST,
        FAVORITE_REFRESH,
        CLEAR_MAP
    }

    private val _marts = MutableLiveData<List<Mart>>()
    val marts: LiveData<List<Mart>>
        get() = _marts

    private val _progressView = MutableLiveData<Int>().apply { value = View.INVISIBLE }
    val progressView: LiveData<Int>
        get() = _progressView

    fun getMarts(northEast: LatLng, southWest: LatLng) {
        marketRepository.getMarts(southWest.latitude, northEast.latitude, southWest.longitude, northEast.longitude)
                .subscribe(
                        {
                            if (it.status == StatusEnum.SUCCESS) {
                                _marts.value = it.result
                                _event.value = Event.MART_LIST
                            }
                        },
                        { throwable -> networkError(throwable) }
                ).apply { addDisposable(this) }
    }

    fun addFavoriteMart(userSeq: Int, mart: Mart) {
        marketRepository.addFavoriteMart(userSeq, mart.martSeq).subscribe(
                { result ->
                    val list = User.getFavoriteMarts()
                    list.add(mart)
                    User.putFavoriteMarts(list)
                    Logger.d("추가 마트 리스트 ${User.getFavoriteMarts()}")
                    _event.value = Event.FAVORITE_REFRESH
                },
                { throwable -> }
        ).apply { addDisposable(this) }
    }

    fun deleteFavoriteMart(userSeq: Int, mart: Mart) {
        marketRepository.deleteFavoriteMart(userSeq, mart.martSeq).subscribe(
                { result ->
                    val list = User.getFavoriteMarts()
                    list.remove(mart)
                    User.putFavoriteMarts(list)
                    _event.value = Event.FAVORITE_REFRESH
                },
                { throwable -> }
        ).apply { addDisposable(this) }
    }

    private fun showProgress() {
        _progressView.value = View.VISIBLE
    }

    private fun hideProgress() {
        _progressView.value = View.INVISIBLE
    }

}