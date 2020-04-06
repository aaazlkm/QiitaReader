package hoge.hogehoge.myapplication.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import hoge.hogehoge.core.di.viewmodel.ViewModelModule
import hoge.hogehoge.myapplication.App
import hoge.hogehoge.myapplication.di.activitymodule.ArticleActivityBuilder
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    AppModule::class,
    ViewModelModule::class,
    DatabaseModule::class,
    DomainModule::class,
    InfraModule::class,
    MarkdownModule::class,
    NetworkModule::class,
    ArticleActivityBuilder::class
])
interface AppComponent : AndroidInjector<App> {
    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): AppComponent
    }

    override fun inject(app: App)
}
