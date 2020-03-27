package hoge.hogehoge.myapplication.di.activitymodule

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap
import hoge.hogehoge.myapplication.di.viewmodel.ViewModelKey
import hoge.hogehoge.myapplication.presentation.article.ArticleActivity
import hoge.hogehoge.myapplication.presentation.article.article.ArticleFragment
import hoge.hogehoge.myapplication.presentation.article.article.ArticleViewModel
import hoge.hogehoge.myapplication.presentation.article.articlelist.ArticleListFragment
import hoge.hogehoge.myapplication.presentation.article.articlelist.ArticleListViewModel

@Module
interface ArticleActivityModule {
    @Binds
    fun bindsArticleActivity(articleActivity: ArticleActivity): AppCompatActivity

    @ContributesAndroidInjector
    fun contributeArticleFragment(): ArticleFragment

    @ContributesAndroidInjector
    fun contributeArticleListFragment(): ArticleListFragment

    @Binds
    @IntoMap
    @ViewModelKey(ArticleViewModel::class)
    fun bindsArticleViewModel(
        articleViewModel: ArticleViewModel
    ): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ArticleListViewModel::class)
    fun bindsArticleListViewModel(
        articleListViewModel: ArticleListViewModel
    ): ViewModel
}
