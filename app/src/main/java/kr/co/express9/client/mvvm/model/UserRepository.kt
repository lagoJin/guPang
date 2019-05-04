package kr.co.express9.client.mvvm.model

import androidx.lifecycle.LiveData
import io.reactivex.Observable
import kr.co.express9.client.mvvm.model.api.UserAPI
import kr.co.express9.client.mvvm.model.data.User
import kr.co.express9.client.mvvm.model.preference.UserPreferenceDataSource

class UserRepository(
    private val userAPI: UserAPI,
    private val userPreferenceDataSource: UserPreferenceDataSource
) {

    fun getPref(): LiveData<User>? {
        return userPreferenceDataSource.get()
    }

    fun putPref(user: User): Observable<Unit> {
        return Observable.fromCallable { userPreferenceDataSource.put(user) }
    }

    fun deletePref(): Observable<Unit> {
        return Observable.fromCallable { userPreferenceDataSource.delete() }
    }
}