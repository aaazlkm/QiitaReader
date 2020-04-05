package hoge.hogehoge.myapplication.di

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import hoge.hogehoge.core.API_TIME_OUT_UNIT_SECONDS
import hoge.hogehoge.core.QIITA_BASE_URL
import hoge.hogehoge.infra.api.qiita.QiitaService
import io.reactivex.schedulers.Schedulers
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
    fun provideQiitaService(@RetrofitQiita retrofit: Retrofit): hoge.hogehoge.infra.api.qiita.QiitaService {
        return retrofit.create(hoge.hogehoge.infra.api.qiita.QiitaService::class.java)
    }

    @RetrofitQiita
    @Singleton
    @Provides
    fun provideRtrofitQiita(okHttpClient: OkHttpClient, gson: Gson): Retrofit {
        return Retrofit.Builder()
            .baseUrl(hoge.hogehoge.core.QIITA_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(hoge.hogehoge.core.API_TIME_OUT_UNIT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(hoge.hogehoge.core.API_TIME_OUT_UNIT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(hoge.hogehoge.core.API_TIME_OUT_UNIT_SECONDS, TimeUnit.SECONDS)
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

    @Singleton
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()
    }
}
