package kr.co.express9.client.di

import kr.co.express9.client.BuildConfig
import kr.co.express9.client.mvvm.model.*
import kr.co.express9.client.mvvm.model.api.*
import kr.co.express9.client.mvvm.model.preference.MartPreferenceDataSource
import kr.co.express9.client.mvvm.model.preference.NotificationPreferenceDataSource
import kr.co.express9.client.mvvm.model.preference.SuggestionPreferenceDataSource
import kr.co.express9.client.mvvm.model.preference.UserPreferenceDataSource
import kr.co.express9.client.mvvm.view.fragment.HomeFragment
import kr.co.express9.client.mvvm.view.fragment.MartFragment
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

    // UserAPI
    single {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_API_URL + "user/")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(UserAPI::class.java)
    }

    // MartAPI
    single {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_API_URL + "mart/")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(MartAPI::class.java)
    }

    // ProductAPI
    single {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_API_URL + "product/")
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(ProductAPI::class.java)
    }

    // NotificationAPI
    single {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_API_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(NotificationAPI::class.java)
    }

    // CartAPI
    single {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_API_URL)
            .client(get())
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(CartAPI::class.java)
    }

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
    factory { SearchFragment() }
    factory { MartFragment() }
    factory { ProfileFragment() }
}

var viewModelModule = module {
    viewModel { KakaoAddressViewModel() }
    viewModel { KakaoUserViewModel() }
    viewModel { MapViewModel() }
    viewModel { MainViewModel() }
    viewModel { HomeViewModel() }
    viewModel { CartViewModel() }
    viewModel { CartHistoryViewModel() }
    viewModel { TermsViewModel() }
    viewModel { UserViewModel() }
    viewModel { SearchViewModel() }
    viewModel { LeafletViewModel() }
    viewModel { SuggestionViewModel() }
    viewModel { ProductViewModel() }
    viewModel { MartViewModel() }
    viewModel { NotificationViewModel() }
}

var repositoryModule = module {
    single { SuggestionRepository() }
    single { KakaoRepository(get()) }
    single { UserRepository() }
    single { MapRepository(get()) }
    single { ProductRepository() }
    single { MartRepository() }
    single { NotificationRepository() }
    single { CartRepository() }
}

var preferenceDataSourceModule = module {
    single { UserPreferenceDataSource() }
    single { MartPreferenceDataSource() }
    single { SuggestionPreferenceDataSource() }
    single { NotificationPreferenceDataSource() }
}

var diModule = listOf(
    apiModule,
    fragmentModule,
    repositoryModule,
    viewModelModule,
    preferenceDataSourceModule
)