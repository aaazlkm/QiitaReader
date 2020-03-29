package hoge.hogehoge.myapplication.di

import dagger.Module
import dagger.Provides
import hoge.hogehoge.myapplication.API_TIME_OUT_IN_SECONDS
import hoge.hogehoge.myapplication.QIITA_BASE_URL
import hoge.hogehoge.myapplication.infra.api.qiita.QiitaService
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
open class NetworkModule {
    companion object {
        val instance = NetworkModule()
    }

    @Singleton
    @Provides
    fun provideQiitaService(@RetrofitQiita retrofit: Retrofit): QiitaService {
        return retrofit.create(QiitaService::class.java)
    }

    @RetrofitQiita
    @Singleton
    @Provides
    fun provideRtrofitQiita(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(QIITA_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createAsync())
            .build()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(API_TIME_OUT_IN_SECONDS, TimeUnit.SECONDS)
            .readTimeout(API_TIME_OUT_IN_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(API_TIME_OUT_IN_SECONDS, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    @Singleton
    @Provides
    fun provideHttpLoggingIntercepter(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }
}
