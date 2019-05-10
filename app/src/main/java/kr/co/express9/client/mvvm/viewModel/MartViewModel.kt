package kr.co.express9.client.mvvm.viewModel

import kr.co.express9.client.base.BaseViewModel
import kr.co.express9.client.mvvm.model.MartRepository
import kr.co.express9.client.mvvm.model.data.Mart
import kr.co.express9.client.mvvm.model.data.User
import kr.co.express9.client.mvvm.model.enumData.StatusEnum
import kr.co.express9.client.util.extension.networkError
import org.koin.standalone.inject

class MartViewModel : BaseViewModel<MartViewModel.Event>() {

    private val martRepository: MartRepository by inject()

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

}