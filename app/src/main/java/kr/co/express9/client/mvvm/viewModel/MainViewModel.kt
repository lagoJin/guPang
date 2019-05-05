package kr.co.express9.client.mvvm.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kr.co.express9.client.R
import kr.co.express9.client.base.BaseViewModel

class MainViewModel : BaseViewModel<MainViewModel.Event>() {
    enum class Event {
        SESSION_CLOSED
    }

    /**
     * BottomNavigation
     */
    private val _selectedBottomNavigationItemId = MutableLiveData<Int>().apply {
        value = R.id.bn_home
    }
    val selectedBottomNavigationItemId: LiveData<Int>
        get() = _selectedBottomNavigationItemId

    fun setSelectedBottomNavigationItemId(itemId: Int) {
        _selectedBottomNavigationItemId.value = itemId
    }
}