package hoge.hogehoge.myapplication

import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import hoge.hogehoge.myapplication.di.DaggerAppComponent
import io.reactivex.plugins.RxJavaPlugins
import timber.log.Timber

class App : DaggerApplication() {

    override fun onCreate() {
        super.onCreate()

        setupTimber()

        setRxJavaPluginsErrorHandler()
    }

    private fun setupTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    private fun setRxJavaPluginsErrorHandler() {
        // disposed後にエラーが呼ばれた場合、このメソッドが呼ばれる
        // 講読解除後のエラーには関心がないはずなので、ここではログ出力のみにする
        RxJavaPlugins.setErrorHandler {
            Timber.e("catch GlobalErrorHandler ${it.message}")
        }
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder()
            .application(this)
            .build()
    }
}
