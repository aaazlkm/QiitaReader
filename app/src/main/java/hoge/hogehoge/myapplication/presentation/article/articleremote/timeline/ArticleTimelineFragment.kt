package hoge.hogehoge.myapplication.presentation.article.articleremote.timeline

import androidx.lifecycle.ViewModelProvider
import hoge.hogehoge.myapplication.di.viewmodel.ViewModelFactory
import hoge.hogehoge.myapplication.presentation.article.articleremote.ArticleRemoteFragment
import hoge.hogehoge.myapplication.presentation.article.articleremote.ArticleRemoteViewModel

class ArticleTimelineFragment : ArticleRemoteFragment() {
    companion object {
        fun newInstance() = ArticleTimelineFragment()
    }

    override fun createViewModel(viewModelFactory: ViewModelFactory): ArticleRemoteViewModel {
        return ViewModelProvider(this, viewModelFactory).get(ArticleTimelineViewModel::class.java)
    }
}
