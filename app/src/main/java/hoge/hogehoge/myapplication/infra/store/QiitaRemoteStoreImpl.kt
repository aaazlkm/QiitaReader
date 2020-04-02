package hoge.hogehoge.myapplication.infra.store

import hoge.hogehoge.myapplication.infra.api.qiita.QiitaService
import hoge.hogehoge.myapplication.infra.api.qiita.api.GetArticleAPI
import hoge.hogehoge.myapplication.infra.api.qiita.api.GetArticlesAPI
import hoge.hogehoge.myapplication.infra.api.qiita.model.ArticleInAPI
import io.reactivex.Single
import javax.inject.Inject

class QiitaRemoteStoreImpl @Inject constructor(
    private val qiitaService: QiitaService
) : QiitaRemoteStore {
    override fun fetchArticle(request: GetArticleAPI.Request): Single<ArticleInAPI> {
        return qiitaService.fetchArticle(request.path)
    }

    override fun fetchArticles(request: GetArticlesAPI.Request): Single<List<ArticleInAPI>> {
        return qiitaService.fetchArticles(request.path, request.parameters)
    }
}
