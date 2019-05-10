package kr.co.express9.client.mvvm.viewModel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kr.co.express9.client.R
import kr.co.express9.client.base.BaseViewModel

class MainViewModel : BaseViewModel<MainViewModel.Event>() {
    enum class Event {
        CHANGE_FAVORITE_MART
    }

    private val _progressView = MutableLiveData<Int>().apply { value = View.INVISIBLE }
    val progressView: LiveData<Int>
        get() = _progressView

    /**
     * BottomNavigation
     */
    private val _selectedBottomNavigationItemId = MutableLiveData<Int>().apply {
        value = R.id.bn_home
    }
    val selectedBottomNavigationItemId: LiveData<Int>
        get() = _selectedBottomNavigationItemId

    fun setSelectedBottomNavigationItemId(itemId: Int) {
        if(_selectedBottomNavigationItemId.value != itemId) _selectedBottomNavigationItemId.value = itemId
    }

    fun setChangeFavoriteMartEvent() {
        _event.value = Event.CHANGE_FAVORITE_MART
    }

    fun showProgress() {
        _progressView.value = View.VISIBLE
    }

    fun hideProgress() {
        _progressView.value = View.INVISIBLE
    }
}