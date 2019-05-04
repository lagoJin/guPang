package kr.co.express9.client.mvvm.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kr.co.express9.client.base.BaseViewModel

class TermsViewModel: BaseViewModel<TermsViewModel.Event>() {

    enum class Event {
        AGREE,
        DISAGREE
    }

    // two-way binding
    val requiredTermsAgreement = MutableLiveData<Boolean>().apply { value = false }

    private val _marketingAgreement = MutableLiveData<Boolean>().apply { value = false }
    val marketingAgreement: LiveData<Boolean>
        get() = _marketingAgreement

    /**
     * 약관 데이터 조회 (수정 예정)
     */
    fun get() {
        // 약관 데이터 불러오기 (수정 예정)
    }

    /**
     * 동의하기
     */
    fun agreement() {
        if(requiredTermsAgreement.value!!) _event.value = Event.AGREE
        else _event.value = Event.DISAGREE
    }
}