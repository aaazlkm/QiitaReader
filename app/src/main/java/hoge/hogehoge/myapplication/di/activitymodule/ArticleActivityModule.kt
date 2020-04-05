package hoge.hogehoge.myapplication.di.activitymodule

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import hoge.hogehoge.core.di.viewmodel.ViewModelKey

@Module
interface ArticleActivityModule {
    @Binds
    fun bindsArticleActivity(articleActivity: hoge.hogehoge.presentation.article.ArticleActivity): AppCompatActivity

    @ContributesAndroidInjector
    fun contributeArticleRemoteViewerFragment(): hoge.hogehoge.presentation.article.articleremoteviewer.ArticleRemoteViewerFragment

    @ContributesAndroidInjector
    fun contributeArticleSavedViewerFragment(): hoge.hogehoge.presentation.article.articlesavedviewer.ArticleSavedViewerFragment

    @ContributesAndroidInjector
    fun contributeArticlePagerFragment(): hoge.hogehoge.presentation.article.articlepager.ArticlePagerFragment

    @ContributesAndroidInjector
    fun contributeArticleTrendFragment(): hoge.hogehoge.presentation.article.articleremote.trend.ArticleTrendFragment

    @ContributesAndroidInjector
    fun contributeArticlePopularFragment(): hoge.hogehoge.presentation.article.articleremote.popular.ArticlePopularFragment

    @ContributesAndroidInjector
    fun contributeArticleTimelineFragment(): hoge.hogehoge.presentation.article.articleremote.timeline.ArticleTimelineFragment

    @ContributesAndroidInjector
    fun contributeArticleSavedFragment(): hoge.hogehoge.presentation.article.articlesaved.ArticleSavedFragment

    @Binds
    @IntoMap
    @ViewModelKey(hoge.hogehoge.presentation.article.articleremoteviewer.ArticleRemoteViewerViewModel::class)
    fun bindsArticleRemoteViewerViewModel(
        articleRemoteViewModel: hoge.hogehoge.presentation.article.articleremoteviewer.ArticleRemoteViewerViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(hoge.hogehoge.presentation.article.articlesavedviewer.ArticleSavedViewerViewModel::class)
    fun bindsArticleSavedViewerViewModel(
        articleSavedViewModel: hoge.hogehoge.presentation.article.articlesavedviewer.ArticleSavedViewerViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(hoge.hogehoge.presentation.article.articleremote.trend.ArticleTrendViewModel::class)
    fun bindsArticleTrendViewModel(
        articleTrendViewModel: hoge.hogehoge.presentation.article.articleremote.trend.ArticleTrendViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(hoge.hogehoge.presentation.article.articleremote.popular.ArticlePopularViewModel::class)
    fun bindsArticlePopularViewModel(
        articlePopularViewModel: hoge.hogehoge.presentation.article.articleremote.popular.ArticlePopularViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(hoge.hogehoge.presentation.article.articleremote.timeline.ArticleTimelineViewModel::class)
    fun bindsArticleTimelineViewModel(
        articleTimelineViewModel: hoge.hogehoge.presentation.article.articleremote.timeline.ArticleTimelineViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(hoge.hogehoge.presentation.article.articlesaved.ArticleSavedViewModel::class)
    fun bindsArticleSavedViewModel(
        articleSavedViewModel: hoge.hogehoge.presentation.article.articlesaved.ArticleSavedViewModel
    ): ViewModel
}
