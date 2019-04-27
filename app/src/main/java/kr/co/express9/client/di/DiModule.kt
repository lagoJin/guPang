package kr.co.express9.client.di

import kr.co.express9.client.BuildConfig
import kr.co.express9.client.mvvm.model.KakaoRepository
import kr.co.express9.client.mvvm.model.MapRepository
import kr.co.express9.client.mvvm.model.SuggestionRepository
import kr.co.express9.client.mvvm.model.UserRepository
import kr.co.express9.client.mvvm.model.api.KakaoAPI
import kr.co.express9.client.mvvm.model.preference.SuggestionPreferenceDataSource
import kr.co.express9.client.mvvm.model.preference.UserPreferenceDataSource
import kr.co.express9.client.mvvm.model.remote.KakaoRemoteDataSource
import kr.co.express9.client.mvvm.model.remote.MapRemoteDataSource
import kr.co.express9.client.mvvm.model.remote.UserRemoteDataSource
import kr.co.express9.client.mvvm.view.fragment.HomeFragment
import kr.co.express9.client.mvvm.view.fragment.MarketFragment
import kr.co.express9.client.mvvm.view.fragment.ProfileFragment
import kr.co.express9.client.mvvm.view.fragment.SearchFragment
import kr.co.express9.client.mvvm.viewModel.*
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
            .baseUrl(BuildConfig.KAKAO_API_URL)
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

var fragmentModule = module {
    factory { HomeFragment() }
    factory { SearchFragment(get()) }
    factory { MarketFragment() }
    factory { ProfileFragment() }
}

var viewModelModule = module {
    viewModel { KakaoAddressViewModel() }
    viewModel { KakaoUserViewModel() }
    viewModel { MapViewModel() }
    viewModel { MainViewModel() }
    viewModel { TermsViewModel() }
    viewModel { UserViewModel() }
    viewModel { CategoryGoodsViewModel() }
    viewModel { LeafletViewModel() }
    viewModel { SuggestionViewModel(get()) }
}

var repositoryModule = module {
    single { SuggestionRepository(get()) }
    single { KakaoRepository(get()) }
    single { UserRepository(get(), get()) }
    single { MapRepository(get()) }
}

var remoteDataSourceModule = module {
    single { MapRemoteDataSource() }
    single { UserRemoteDataSource() }
    single { KakaoRemoteDataSource(get()) }
}

var preferenceDataSourceModule = module {
    single { UserPreferenceDataSource() }
    single { SuggestionPreferenceDataSource() }
}

var diModule = listOf(
    apiModule,
    fragmentModule,
    repositoryModule,
    viewModelModule,
    remoteDataSourceModule,
    preferenceDataSourceModule
)