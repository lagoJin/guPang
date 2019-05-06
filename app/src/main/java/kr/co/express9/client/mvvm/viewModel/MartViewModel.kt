package kr.co.express9.client.mvvm.viewModel

import kr.co.express9.client.base.BaseViewModel
import kr.co.express9.client.mvvm.model.MartRepository
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
        martRepository.deleteFavoriteMart(martSeq).subscribe(
            {
                if(it.status == StatusEnum.SUCCESS){

                    _event.value = Event.MART_DELETE
                }

            }, { throwable ->
                networkError(throwable)
            }
        ).apply {
            addDisposable(this)
        }
    }

}