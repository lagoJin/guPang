package kr.co.express9.client.mvvm.model

import androidx.lifecycle.LiveData
import io.reactivex.Observable
import kr.co.express9.client.mvvm.model.api.MarketAPI
import kr.co.express9.client.mvvm.model.data.User
import kr.co.express9.client.mvvm.model.preference.UserPreferenceDataSource

class UserRepository(
    private val marketAPI: MarketAPI,
    private val userPreferenceDataSource: UserPreferenceDataSource
) {

    /**
     * Preference
     */
    fun getPref(): LiveData<User>? {
        return userPreferenceDataSource.get()
    }

    fun putPref(user: User): Observable<Unit> {
        return Observable.fromCallable { userPreferenceDataSource.put(user) }
    }

    fun deletePref(): Observable<Unit> {
        return Observable.fromCallable { userPreferenceDataSource.delete() }
    }

    /**
     * MarketAPI
     */
//    fun postUserLogin
}