package hoge.hogehoge.myapplication.presentation.article.articlelist.trend

import hoge.hogehoge.myapplication.domain.entity.Article
import hoge.hogehoge.myapplication.domain.result.Result
import hoge.hogehoge.myapplication.domain.usecase.QiitaUseCase
import hoge.hogehoge.myapplication.presentation.article.articlelist.ArticleListViewModel
import io.reactivex.Observable
import javax.inject.Inject

class ArticleTrendViewModel @Inject constructor(
    private val qiitaUseCase: QiitaUseCase
) : ArticleListViewModel() {

    override fun getArticleDataSource(page: Int, perPage: Int): Observable<Result<List<Article.Remote>>> {
        return qiitaUseCase.fetchArticles(page, perPage)
    }
}
