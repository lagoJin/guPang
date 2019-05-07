package kr.co.express9.client.mvvm.viewModel

import android.app.SearchManager
import android.database.Cursor
import android.database.MatrixCursor
import android.provider.BaseColumns
import kr.co.express9.client.base.BaseViewModel
import kr.co.express9.client.mvvm.model.SuggestionRepository
import kr.co.express9.client.util.Logger
import org.koin.standalone.inject

class SuggestionViewModel : BaseViewModel<SuggestionViewModel.Event>() {
    private val suggestionRepository: SuggestionRepository by inject()

    enum class Event {

    }

    var suggestedList = ArrayList<String>()

    fun getSuggestionCursor(search: String): Cursor {
        suggestedList.clear()
        val cursor = MatrixCursor(arrayOf(BaseColumns._ID,
                SearchManager.SUGGEST_COLUMN_TEXT_1,
                SearchManager.SUGGEST_COLUMN_INTENT_DATA))

        suggestionRepository.suggestionList.forEachIndexed { index, str ->
            if (str.contains(search, ignoreCase = true)) {
                cursor.addRow(arrayOf(index, str, null))
                suggestedList.add(str)
            }
        }
        Logger.d("getSuggestionCursor $search / $suggestedList / ${suggestionRepository.suggestionList}")
        return cursor
    }

    fun putSuggestion(suggestion: String) {
        suggestionRepository.putPref(suggestion)
    }
}