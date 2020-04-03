package hoge.hogehoge.myapplication.presentation.article.articlelist.trend

import androidx.lifecycle.ViewModelProvider
import hoge.hogehoge.myapplication.di.viewmodel.ViewModelFactory
import hoge.hogehoge.myapplication.presentation.article.articlelist.ArticleListFragment
import hoge.hogehoge.myapplication.presentation.article.articlelist.ArticleListViewModel

class ArticleTrendFragment : ArticleListFragment() {
    companion object {
        fun newInstance() = ArticleTrendFragment()
    }

    override fun createViewModel(viewModelFactory: ViewModelFactory): ArticleListViewModel {
        return ViewModelProvider(this, viewModelFactory).get(ArticleTrendViewModel::class.java)
    }
}
