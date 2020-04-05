package hoge.hogehoge.presentation.article.articleremote.popular

import androidx.lifecycle.ViewModelProvider
import hoge.hogehoge.core.di.viewmodel.ViewModelFactory
import hoge.hogehoge.presentation.article.articleremote.ArticleRemoteFragment
import hoge.hogehoge.presentation.article.articleremote.ArticleRemoteViewModel

class ArticlePopularFragment : ArticleRemoteFragment() {
    companion object {
        fun newInstance() = ArticlePopularFragment()
    }

    override fun createViewModel(viewModelFactory: ViewModelFactory): ArticleRemoteViewModel {
        return ViewModelProvider(this, viewModelFactory).get(ArticlePopularViewModel::class.java)
    }
}
