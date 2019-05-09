package kr.co.express9.client.mvvm.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kr.co.express9.client.base.BaseViewModel
import kr.co.express9.client.mvvm.model.NotificationRepository
import kr.co.express9.client.mvvm.model.data.Notification
import kr.co.express9.client.mvvm.model.enumData.StatusEnum
import kr.co.express9.client.util.Logger
import org.koin.standalone.inject

class NotificationViewModel : BaseViewModel<NotificationViewModel.Event>() {

    private val notificationRepository: NotificationRepository by inject()

    enum class Event {
        ITEM_NAME_IS_NULL,
        ALREADY_HAS_NOTIFICATION
    }

    val itemName: MutableLiveData<String> = MutableLiveData() // two way binding

    private val _notificationList = MutableLiveData<ArrayList<Notification>>()
    val notificationList: LiveData<ArrayList<Notification>>
        get() = _notificationList

    private val _isNotification = MutableLiveData<Boolean>().apply { value = false }
    val isNotification: LiveData<Boolean>
        get() = _isNotification

    fun getNotifications() {
        notificationRepository.getNotifications()
                .subscribe({
                    if (it.status == StatusEnum.SUCCESS) {
                        _notificationList.value = it.result
                        isNotification()
                    }
                }, {
                    Logger.d(it.toString())
                })
                .apply { addDisposable(this) }
    }

    fun addNotification(cb: (isSuccess: Boolean) -> Unit) {
        if (itemName.value == null) {
            _event.value = Event.ITEM_NAME_IS_NULL
            return
        }

        // 이미 동일 항목이 있는 경우
        _notificationList.value!!.forEach {
            if(it.text == itemName.value) {
                _event.value = Event.ALREADY_HAS_NOTIFICATION
                return@addNotification
            }
        }
        notificationRepository.addNotification(itemName.value!!)
                .subscribe({
                    cb(it.status == StatusEnum.SUCCESS)
                    if(it.status == StatusEnum.SUCCESS) getNotifications()
                }, {
                    Logger.d(it.toString())
                })
                .apply { addDisposable(this) }
    }

    fun deleteNotification(index: Int, cb: (isSuccess: Boolean) -> Unit) {
        notificationRepository.deleteNotification(_notificationList.value!![index].text)
                .subscribe({
                    cb(it.status == StatusEnum.SUCCESS)
                    if(it.status == StatusEnum.SUCCESS) getNotifications()
                }, {
                    Logger.d(it.toString())
                })
                .apply { addDisposable(this) }
    }

    private fun isNotification() {
        _isNotification.value = _notificationList.value!!.size > 0
    }


}