package kr.co.express9.client.base

import android.app.Application
import com.kakao.auth.KakaoSDK
import com.orhanobut.hawk.Hawk
import kr.co.express9.client.BuildConfig
import kr.co.express9.client.R
import kr.co.express9.client.thirdParty.kakao.KakaoSDKAdapter
import kr.co.express9.client.util.Logger
import kr.co.express9.client.di.diModule
import org.koin.android.ext.android.startKoin

class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        Logger.init(BuildConfig.DEBUG, getString(R.string.app_name))
        startKoin(this, diModule)
        KakaoSDK.init(KakaoSDKAdapter(this))
        Hawk.init(this).build()
    }
}