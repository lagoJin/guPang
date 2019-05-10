package kr.co.express9.client.mvvm.viewModel

import android.app.SearchManager
import android.database.Cursor
import android.database.MatrixCursor
import android.provider.BaseColumns
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kr.co.express9.client.base.BaseViewModel
import kr.co.express9.client.mvvm.model.SuggestionRepository
import kr.co.express9.client.util.Logger
import org.koin.standalone.inject

class SuggestionViewModel : BaseViewModel<SuggestionViewModel.Event>() {
    private val suggestionRepository: SuggestionRepository by inject()

    enum class Event {

    }

    private val _progressView = MutableLiveData<Int>().apply { value = View.INVISIBLE }
    val progressView: LiveData<Int>
        get() = _progressView

    private var _selectedSuggestion = MutableLiveData<String?>()
    val selectedSuggestion: LiveData<String?>
        get() = _selectedSuggestion

    private var suggestedList = ArrayList<String>()

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
        return cursor
    }

    fun putSuggestion(suggestion: String?) {
        suggestion?.let { suggestionRepository.putPref(it) }
        _selectedSuggestion.value = suggestion
    }

    fun selectSuggestion(index: Int): String {
        _selectedSuggestion.value = suggestedList[index]
        return suggestedList[index]
    }

    private fun showProgress() {
        _progressView.value = View.VISIBLE
    }

    private fun hideProgress() {
        _progressView.value = View.INVISIBLE
    }
}