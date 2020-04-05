package hoge.hogehoge.presentation.article.articleremote.timeline

import hoge.hogehoge.domain.entity.Article
import hoge.hogehoge.domain.result.Result
import hoge.hogehoge.domain.usecase.QiitaUseCase
import hoge.hogehoge.presentation.article.articleremote.ArticleRemoteViewModel
import io.reactivex.Observable
import javax.inject.Inject

class ArticleTimelineViewModel @Inject constructor(
    private val qiitaUseCase: QiitaUseCase
) : ArticleRemoteViewModel() {

    override fun getArticleDataSource(page: Int, perPage: Int): Observable<Result<List<Article.Remote>>> {
        return qiitaUseCase.fetchTimelineArticles(page, perPage)
    }
}
