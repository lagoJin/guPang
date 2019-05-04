package kr.co.express9.client.base

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.util.Base64
import com.kakao.auth.KakaoSDK
import com.kakao.util.helper.Utility.getPackageInfo
import com.orhanobut.hawk.Hawk
import kr.co.express9.client.BuildConfig
import kr.co.express9.client.di.diModule
import kr.co.express9.client.thirdParty.kakao.KakaoSDKAdapter
import kr.co.express9.client.util.Logger
import org.koin.android.ext.android.startKoin
import java.security.MessageDigest


class BaseApplication : Application() {

    companion object {

        internal lateinit var context : Context
    }

    override fun onCreate() {
        super.onCreate()
        context = baseContext
        Logger.init(BuildConfig.DEBUG, getString(kr.co.express9.client.R.string.app_name))
        startKoin(this, diModule)
        KakaoSDK.init(KakaoSDKAdapter(this))
        Hawk.init(this)
                .setLogInterceptor { Logger.d("Preference Logger :: $it") }
                .build()
        getKeyHash()
    }

    private fun getKeyHash() {
        val packageInfo = getPackageInfo(this, PackageManager.GET_SIGNATURES)

        for (signature in packageInfo!!.signatures) {
            val md = MessageDigest.getInstance("SHA")
            md.update(signature.toByteArray())
            Logger.d("HashKey :  ${Base64.encodeToString(md.digest(), Base64.NO_WRAP)}")
        }
    }

}