package kr.co.express9.client.mvvm.viewModel

import android.app.SearchManager
import android.database.Cursor
import android.database.MatrixCursor
import android.provider.BaseColumns
import kr.co.express9.client.base.BaseViewModel
import kr.co.express9.client.util.Logger

class SuggestionViewModel : BaseViewModel() {

    private val suggestionList = ArrayList<String>().apply {
        add("좋은 계란")
        add("맛없는 사과")
        add("맛있는 배")
        add("물컵")
        add("컵테스트")
    }

    val filteredList = ArrayList<String>()

    // suggestion 목록(content provider 대용)
    fun getSuggestionCursor(s: String): Cursor {
        filteredList.clear()
        val cursor = MatrixCursor(
            arrayOf(
                BaseColumns._ID,
                SearchManager.SUGGEST_COLUMN_TEXT_1,
                SearchManager.SUGGEST_COLUMN_INTENT_DATA
            )
        )

        suggestionList
            .forEachIndexed { index, str ->
                if (str.contains(s, ignoreCase = true)) {
                    cursor.addRow(arrayOf(index, str, null))
                    filteredList.add(str)
                }
            }
        Logger.d("들어온다2 : $filteredList")

        return cursor
    }
}