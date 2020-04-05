package hoge.hogehoge.infra.store

import hoge.hogehoge.infra.api.qiita.api.GetArticleAPI
import hoge.hogehoge.infra.api.qiita.api.GetArticlesAPI
import hoge.hogehoge.infra.api.qiita.model.ArticleInAPI
import io.reactivex.Single

interface QiitaRemoteStore {
    fun fetchArticle(request: GetArticleAPI.Request): Single<ArticleInAPI>

    fun fetchArticles(request: GetArticlesAPI.Request): Single<List<ArticleInAPI>>
}
