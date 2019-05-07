package kr.co.express9.client.mvvm.model

import kr.co.express9.client.mvvm.model.preference.SuggestionPreferenceDataSource
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class SuggestionRepository: KoinComponent {
    private val searchPreferenceDataSource: SuggestionPreferenceDataSource by inject()

    var suggestionList: ArrayList<String> = getPref()

    private fun getPref(): ArrayList<String> {
        return searchPreferenceDataSource.get()
    }

    fun putPref(suggestion: String) {
        if (suggestion !in suggestionList) suggestionList.add(suggestion)
        searchPreferenceDataSource.put(suggestionList)
    }

    fun deletePref() {
        searchPreferenceDataSource.delete()
        suggestionList = ArrayList()
    }

}