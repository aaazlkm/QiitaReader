package hoge.hogehoge.myapplication.di.activitymodule

import dagger.Module
import dagger.android.ContributesAndroidInjector
import hoge.hogehoge.presentation.article.ArticleActivity

@Module
interface ArticleActivityBuilder {
    @ContributesAndroidInjector(
        modules = [ArticleActivityModule::class]
    )
    fun contributeCalendarActivity(): ArticleActivity
}
