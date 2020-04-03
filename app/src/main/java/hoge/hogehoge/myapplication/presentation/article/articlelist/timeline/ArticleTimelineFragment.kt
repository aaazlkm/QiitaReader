package hoge.hogehoge.myapplication.presentation.article.articlelist.timeline

import androidx.lifecycle.ViewModelProvider
import hoge.hogehoge.myapplication.di.viewmodel.ViewModelFactory
import hoge.hogehoge.myapplication.presentation.article.articlelist.ArticleListFragment
import hoge.hogehoge.myapplication.presentation.article.articlelist.ArticleListViewModel

class ArticleTimelineFragment : ArticleListFragment() {
    companion object {
        fun newInstance() = ArticleTimelineFragment()
    }

    override fun createViewModel(viewModelFactory: ViewModelFactory): ArticleListViewModel {
        return ViewModelProvider(this, viewModelFactory).get(ArticleTimelineViewModel::class.java)
    }
}
