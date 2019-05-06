package kr.co.express9.client.mvvm.model.preference

import androidx.lifecycle.MutableLiveData
import com.orhanobut.hawk.Hawk
import kr.co.express9.client.mvvm.model.data.Mart

class MartPreferenceDataSource {

    private enum class MartPref(key: String) {
        FAVORITE_MARTS("FAVORITE_MARTS")
    }

    fun getFavoriteMarts(): MutableLiveData<ArrayList<Mart>>? {
        return if (Hawk.contains(MartPref.FAVORITE_MARTS.name)) {
            MutableLiveData<ArrayList<Mart>>().apply {
                value = Hawk.get(MartPref.FAVORITE_MARTS.name)
            }
        } else {
            null
        }
    }

    fun putFavoriteMarts(favoriteMartList: ArrayList<Mart>) {
        Hawk.put(MartPref.FAVORITE_MARTS.name, favoriteMartList)
    }

    fun deleteFavoriteMarts() {
        Hawk.delete(MartPref.FAVORITE_MARTS.name)
    }
}