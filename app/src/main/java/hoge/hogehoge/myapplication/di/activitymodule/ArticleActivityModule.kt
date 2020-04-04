package hoge.hogehoge.myapplication.di.activitymodule

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import hoge.hogehoge.myapplication.di.viewmodel.ViewModelKey
import hoge.hogehoge.myapplication.presentation.article.ArticleActivity
import hoge.hogehoge.myapplication.presentation.article.articlepager.ArticlePagerFragment
import hoge.hogehoge.myapplication.presentation.article.articleremote.popular.ArticlePopularFragment
import hoge.hogehoge.myapplication.presentation.article.articleremote.popular.ArticlePopularViewModel
import hoge.hogehoge.myapplication.presentation.article.articleremote.timeline.ArticleTimelineFragment
import hoge.hogehoge.myapplication.presentation.article.articleremote.timeline.ArticleTimelineViewModel
import hoge.hogehoge.myapplication.presentation.article.articleremote.trend.ArticleTrendFragment
import hoge.hogehoge.myapplication.presentation.article.articleremote.trend.ArticleTrendViewModel
import hoge.hogehoge.myapplication.presentation.article.articlesaved.ArticleSavedFragment
import hoge.hogehoge.myapplication.presentation.article.articlesaved.ArticleSavedViewModel
import hoge.hogehoge.myapplication.presentation.article.articleviewer.ArticleViewerFragment
import hoge.hogehoge.myapplication.presentation.article.articleviewer.ArticleViewerViewModel

@Module
interface ArticleActivityModule {
    @Binds
    fun bindsArticleActivity(articleActivity: ArticleActivity): AppCompatActivity

    @ContributesAndroidInjector
    fun contributeArticleViewerFragment(): ArticleViewerFragment

    @ContributesAndroidInjector
    fun contributeArticlePagerFragment(): ArticlePagerFragment

    @ContributesAndroidInjector
    fun contributeArticleTrendFragment(): ArticleTrendFragment

    @ContributesAndroidInjector
    fun contributeArticlePopularFragment(): ArticlePopularFragment

    @ContributesAndroidInjector
    fun contributeArticleTimelineFragment(): ArticleTimelineFragment

    @ContributesAndroidInjector
    fun contributeArticleSavedFragment(): ArticleSavedFragment

    @Binds
    @IntoMap
    @ViewModelKey(ArticleViewerViewModel::class)
    fun bindsArticleViewerViewModel(
        articleViewModel: ArticleViewerViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ArticleTrendViewModel::class)
    fun bindsArticleTrendViewModel(
        articleTrendViewModel: ArticleTrendViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ArticlePopularViewModel::class)
    fun bindsArticlePopularViewModel(
        articlePopularViewModel: ArticlePopularViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ArticleTimelineViewModel::class)
    fun bindsArticleTimelineViewModel(
        articleTimelineViewModel: ArticleTimelineViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ArticleSavedViewModel::class)
    fun bindsArticleSavedViewModel(
        articleSavedViewModel: ArticleSavedViewModel
    ): ViewModel
}
