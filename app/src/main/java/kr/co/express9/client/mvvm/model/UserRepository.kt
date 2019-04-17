package kr.co.express9.client.mvvm.model

import androidx.lifecycle.LiveData
import io.reactivex.Observable
import kr.co.express9.client.mvvm.model.data.User
import kr.co.express9.client.mvvm.model.preference.UserPreferenceDataSource
import kr.co.express9.client.mvvm.model.remote.UserRemoteDataSource
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class UserRepository : KoinComponent {

    private val userRemoteDataSource: UserRemoteDataSource by inject()
    private val userPreferenceDataSource: UserPreferenceDataSource by inject()


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