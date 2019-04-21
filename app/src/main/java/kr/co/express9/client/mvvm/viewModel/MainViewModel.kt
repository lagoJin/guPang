package kr.co.express9.client.mvvm.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kr.co.express9.client.R
import kr.co.express9.client.base.BaseViewModel
import kr.co.express9.client.util.Logger

class MainViewModel : BaseViewModel() {
    enum class Event {
        SESSION_CLOSED
    }

    private val _event = MutableLiveData<Event>()
    val event: LiveData<Event>
        get() = _event

    private val _selectedItemId = MutableLiveData<Int>().apply {
        Logger.d("들어온다아아")
        value = R.id.main
    }
    val selectedItemId: LiveData<Int>
        get() = _selectedItemId

    fun setSelectedItemId(itemId: Int) {
        _selectedItemId.value = itemId
    }
}