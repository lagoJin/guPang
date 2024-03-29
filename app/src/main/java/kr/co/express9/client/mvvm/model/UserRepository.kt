package kr.co.express9.client.mvvm.model

import androidx.lifecycle.LiveData
import io.reactivex.Observable
import io.reactivex.Single
import kr.co.express9.client.mvvm.model.api.UserAPI
import kr.co.express9.client.mvvm.model.data.Response
import kr.co.express9.client.mvvm.model.data.ResultNeedModify
import kr.co.express9.client.mvvm.model.data.User
import kr.co.express9.client.mvvm.model.preference.UserPreferenceDataSource
import kr.co.express9.client.util.extension.networkCommunication
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject

class UserRepository: KoinComponent {
    private val userApi: UserAPI by inject()
    private val userPreferenceDataSource: UserPreferenceDataSource by inject()

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
     * Remote
     */
    fun login(uuid: String,
              name: String,
              deviceToken: String): Single<Response<String>> {
        return userApi.login(uuid, name, deviceToken).networkCommunication()
    }

    fun join(uuid: String,
             name: String,
             deviceToken: String): Single<Response<String>> {
        return userApi.join(uuid, name, deviceToken).networkCommunication()
    }

    fun getInfo(userSeq: Int): Single<Response<User>> {
        return userApi.getInfo(userSeq).networkCommunication()
    }
}