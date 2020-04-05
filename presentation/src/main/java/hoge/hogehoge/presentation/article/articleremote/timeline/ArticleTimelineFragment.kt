package hoge.hogehoge.presentation.article.articleremote.timeline

import androidx.lifecycle.ViewModelProvider
import hoge.hogehoge.core.di.viewmodel.ViewModelFactory
import hoge.hogehoge.presentation.article.articleremote.ArticleRemoteFragment
import hoge.hogehoge.presentation.article.articleremote.ArticleRemoteViewModel

class ArticleTimelineFragment : ArticleRemoteFragment() {
    companion object {
        fun newInstance() = ArticleTimelineFragment()
    }

    override fun createViewModel(viewModelFactory: ViewModelFactory): ArticleRemoteViewModel {
        return ViewModelProvider(this, viewModelFactory).get(ArticleTimelineViewModel::class.java)
    }
}
