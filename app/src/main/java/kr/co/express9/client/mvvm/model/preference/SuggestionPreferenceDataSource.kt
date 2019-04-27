package kr.co.express9.client.mvvm.model.preference

import com.orhanobut.hawk.Hawk

class SuggestionPreferenceDataSource {

    private enum class SuggestionPref(key: String) {
        SUGGESTION("SUGGESTION")
    }

    fun get(): ArrayList<String> {
        return if (Hawk.contains(SuggestionPref.SUGGESTION.name)) {
            Hawk.get(SuggestionPref.SUGGESTION.name)
        } else {
            ArrayList()
        }
    }

    fun put(suggestion: ArrayList<String>) {
        Hawk.put(SuggestionPref.SUGGESTION.name, suggestion)
    }

    fun delete() {
        Hawk.delete(SuggestionPref.SUGGESTION.name)
    }
}