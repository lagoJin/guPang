package kr.co.express9.client.mvvm.viewModel

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kr.co.express9.client.base.BaseViewModel
import kr.co.express9.client.mvvm.model.MartRepository
import kr.co.express9.client.mvvm.model.data.Mart
import kr.co.express9.client.mvvm.model.data.User
import kr.co.express9.client.mvvm.model.enumData.StatusEnum
import kr.co.express9.client.util.extension.networkError
import org.koin.standalone.inject

class MartViewModel : BaseViewModel<MartViewModel.Event>() {

    private val martRepository: MartRepository by inject()

    private val _progressView = MutableLiveData<Int>().apply { value = View.INVISIBLE }
    val progressView: LiveData<Int>
        get() = _progressView

    enum class Event {
        MART_ADD,
        MART_DELETE
    }

    fun deleteFavoriteMart(martSeq: Int) {
        martRepository.deleteFavoriteMart(User.getUser().userSeq, martSeq).subscribe(
            {
                if (it.status == StatusEnum.SUCCESS) {
                    _event.value = Event.MART_DELETE
                    removeFavoriteMart(martSeq)
                }

            }, { throwable ->
                networkError(throwable)
            }).apply {
            addDisposable(this)
        }
    }

    private fun removeFavoriteMart(martSeq: Int) {
        val favoriteMarts = User.getFavoriteMarts()
        var mart: Mart? = null
        favoriteMarts.forEach {
            if (it.martSeq == martSeq) {
                mart = it
                return@forEach
            }
        }
        mart?.let { favoriteMarts.remove(it) }
        martRepository.putFavoriteMartsPref(favoriteMarts)
    }

    private fun showProgress() {
        _progressView.value = View.VISIBLE
    }

    private fun hideProgress() {
        _progressView.value = View.INVISIBLE
    }

}