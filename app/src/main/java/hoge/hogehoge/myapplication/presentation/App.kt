package hoge.hogehoge.myapplication.presentation

import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import hoge.hogehoge.myapplication.BuildConfig
import hoge.hogehoge.myapplication.di.DaggerAppComponent
import hoge.hogehoge.myapplication.di.DatabaseModule
import hoge.hogehoge.myapplication.di.DomainModule
import hoge.hogehoge.myapplication.di.InfraModule
import hoge.hogehoge.myapplication.di.NetworkModule
import timber.log.Timber

class App : DaggerApplication() {

    override fun onCreate() {
        super.onCreate()

        setupTimber()
    }

    private fun setupTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder()
            .application(this)
            .databaseModule(DatabaseModule.instance)
            .networkModule(NetworkModule.instance)
            .domainModule(DomainModule.instance)
            .infraModule(InfraModule.instance)
            .build()
    }
}
