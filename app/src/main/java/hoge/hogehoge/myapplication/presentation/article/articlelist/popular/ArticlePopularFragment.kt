package hoge.hogehoge.myapplication.presentation.article.articlelist.popular

import androidx.lifecycle.ViewModelProvider
import hoge.hogehoge.myapplication.di.viewmodel.ViewModelFactory
import hoge.hogehoge.myapplication.presentation.article.articlelist.ArticleListFragment
import hoge.hogehoge.myapplication.presentation.article.articlelist.ArticleListViewModel

class ArticlePopularFragment : ArticleListFragment() {
    companion object {
        fun newInstance() = ArticlePopularFragment()
    }

    override fun createViewModel(viewModelFactory: ViewModelFactory): ArticleListViewModel {
        return ViewModelProvider(this, viewModelFactory).get(ArticlePopularViewModel::class.java)
    }
}
