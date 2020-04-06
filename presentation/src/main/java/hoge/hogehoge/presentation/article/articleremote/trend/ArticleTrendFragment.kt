package hoge.hogehoge.presentation.article.articleremote.trend

import androidx.lifecycle.ViewModelProvider
import hoge.hogehoge.core.di.viewmodel.ViewModelFactory
import hoge.hogehoge.presentation.article.articleremote.ArticleRemoteFragment
import hoge.hogehoge.presentation.article.articleremote.ArticleRemoteViewModel

class ArticleTrendFragment : ArticleRemoteFragment() {
    companion object {
        fun newInstance() = ArticleTrendFragment()
    }

    override fun createViewModel(viewModelFactory: ViewModelFactory): ArticleRemoteViewModel {
        return ViewModelProvider(this, viewModelFactory).get(ArticleTrendViewModel::class.java)
    }
}
