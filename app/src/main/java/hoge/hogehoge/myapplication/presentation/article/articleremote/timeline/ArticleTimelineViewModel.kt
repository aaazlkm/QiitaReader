package hoge.hogehoge.myapplication.presentation.article.articleremote.timeline

import hoge.hogehoge.myapplication.domain.entity.Article
import hoge.hogehoge.myapplication.domain.result.Result
import hoge.hogehoge.myapplication.domain.usecase.QiitaUseCase
import hoge.hogehoge.myapplication.presentation.article.articleremote.ArticleRemoteViewModel
import io.reactivex.Observable
import javax.inject.Inject

class ArticleTimelineViewModel @Inject constructor(
    private val qiitaUseCase: QiitaUseCase
) : ArticleRemoteViewModel() {

    override fun getArticleDataSource(page: Int, perPage: Int): Observable<Result<List<Article.Remote>>> {
        return qiitaUseCase.fetchTimelineArticles(page, perPage)
    }
}
