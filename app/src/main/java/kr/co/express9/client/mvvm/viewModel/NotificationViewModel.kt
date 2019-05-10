package kr.co.express9.client.mvvm.viewModel

import android.view.View
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

    private val _progressView = MutableLiveData<Int>().apply { value = View.INVISIBLE }
    val progressView: LiveData<Int>
        get() = _progressView

    /**
     * Notification
     */
    val itemName: MutableLiveData<String> = MutableLiveData() // two way binding

    private val _notificationList = MutableLiveData<ArrayList<Notification>>()
    val notificationList: LiveData<ArrayList<Notification>>
        get() = _notificationList

    private val _isNotification = MutableLiveData<Boolean>().apply { value = false }
    val isNotification: LiveData<Boolean>
        get() = _isNotification

    /**
     * Notification History
     */
    private val _notificationHistoryList = MutableLiveData<ArrayList<Notification>>()
    val notificationHistoryList: LiveData<ArrayList<Notification>>
        get() = _notificationHistoryList

    private val _isNotificationHistory = MutableLiveData<Boolean>().apply { value = false }
    val isNotificationHistory: LiveData<Boolean>
        get() = _isNotificationHistory

    fun getNotifications() {
        notificationRepository.getNotifications()
                .doOnSubscribe { showProgress() }
                .subscribe({
                    if (it.status == StatusEnum.SUCCESS) {
                        _notificationList.value = it.result
                        isNotification()
                    }
                    hideProgress()
                }, {
                    Logger.d(it.toString())
                    hideProgress()
                }).apply { addDisposable(this) }
    }

    fun addNotification(cb: (isSuccess: Boolean) -> Unit) {
        if (itemName.value == null) {
            _event.value = Event.ITEM_NAME_IS_NULL
            return
        }

        // 이미 동일 항목이 있는 경우
        _notificationList.value!!.forEach {
            if (it.text == itemName.value) {
                _event.value = Event.ALREADY_HAS_NOTIFICATION
                return@addNotification
            }
        }
        notificationRepository.addNotification(itemName.value!!)
                .doOnSubscribe { showProgress() }
                .subscribe({
                    cb(it.status == StatusEnum.SUCCESS)
                    if (it.status == StatusEnum.SUCCESS) getNotifications()
                    hideProgress()
                }, {
                    Logger.d(it.toString())
                    hideProgress()
                })
                .apply { addDisposable(this) }
    }

    fun deleteNotification(index: Int, cb: (isSuccess: Boolean) -> Unit) {
        notificationRepository.deleteNotification(_notificationList.value!![index].text)
                .doOnSubscribe { showProgress() }
                .subscribe({
                    cb(it.status == StatusEnum.SUCCESS)
                    if (it.status == StatusEnum.SUCCESS) getNotifications()
                    hideProgress()
                }, {
                    Logger.d(it.toString())
                    hideProgress()
                }).apply { addDisposable(this) }
    }

    private fun isNotification() {
        _isNotification.value = _notificationList.value!!.size > 0
    }

    private fun showProgress() {
        _progressView.value = View.VISIBLE
    }

    private fun hideProgress() {
        _progressView.value = View.INVISIBLE
    }

    fun getNotificationHistoryPref() {
        val notiHistory = notificationRepository.getNotificationHistoryPref()
        _isNotificationHistory.value = notiHistory!!.value!!.size > 0
        Logger.d("아러나일 ${notiHistory.value}")
        notiHistory.let { _notificationHistoryList.value = it.value }
    }
}