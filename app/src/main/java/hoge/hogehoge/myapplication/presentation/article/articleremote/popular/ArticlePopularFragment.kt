package hoge.hogehoge.myapplication.presentation.article.articleremote.popular

import androidx.lifecycle.ViewModelProvider
import hoge.hogehoge.myapplication.di.viewmodel.ViewModelFactory
import hoge.hogehoge.myapplication.presentation.article.articleremote.ArticleRemoteFragment
import hoge.hogehoge.myapplication.presentation.article.articleremote.ArticleRemoteViewModel

class ArticlePopularFragment : ArticleRemoteFragment() {
    companion object {
        fun newInstance() = ArticlePopularFragment()
    }

    override fun createViewModel(viewModelFactory: ViewModelFactory): ArticleRemoteViewModel {
        return ViewModelProvider(this, viewModelFactory).get(ArticlePopularViewModel::class.java)
    }
}
