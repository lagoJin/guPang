package kr.co.express9.client.util

import kr.co.express9.client.constant.KAKAO_URL
import kr.co.express9.client.mvvm.model.api.KakaoAPI
import kr.co.express9.client.mvvm.viewModel.LoginViewModel
import kr.co.express9.client.mvvm.viewModel.MainViewModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


val apiModule = module {

    // KakaoAPI
    single {
        Retrofit.Builder()
                .baseUrl(KAKAO_URL)
                .client(get())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(KakaoAPI::class.java)
    }

    // OkHttpClient
    single {
        OkHttpClient.Builder().addInterceptor(get() as HttpLoggingInterceptor).build()

    }

    // HttpLoggingInterceptor
    single {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }
}

var viewModelModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { LoginViewModel() }
}

var diModule = listOf(apiModule, viewModelModule)