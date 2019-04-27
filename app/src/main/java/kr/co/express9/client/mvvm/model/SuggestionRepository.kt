package kr.co.express9.client.mvvm.model

import kr.co.express9.client.mvvm.model.preference.SuggestionPreferenceDataSource

class SuggestionRepository(
    private val searchPreferenceDataSource: SuggestionPreferenceDataSource
) {

    var suggestionList: ArrayList<String> = getPref()

    private fun getPref(): ArrayList<String> {
        return searchPreferenceDataSource.get()
    }

    fun putPref(suggestion: String) {
        suggestionList.add(suggestion)
        searchPreferenceDataSource.put(suggestionList)
    }

    fun deletePref() {
        searchPreferenceDataSource.delete()
        suggestionList = ArrayList()
    }

}