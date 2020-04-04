package hoge.hogehoge.myapplication.presentation.article.articleremote.trend

import androidx.lifecycle.ViewModelProvider
import hoge.hogehoge.myapplication.di.viewmodel.ViewModelFactory
import hoge.hogehoge.myapplication.presentation.article.articleremote.ArticleRemoteFragment
import hoge.hogehoge.myapplication.presentation.article.articleremote.ArticleRemoteViewModel

class ArticleTrendFragment : ArticleRemoteFragment() {
    companion object {
        fun newInstance() = ArticleTrendFragment()
    }

    override fun createViewModel(viewModelFactory: ViewModelFactory): ArticleRemoteViewModel {
        return ViewModelProvider(this, viewModelFactory).get(ArticleTrendViewModel::class.java)
    }
}
