package hoge.hogehoge.myapplication.infra.store

import hoge.hogehoge.myapplication.infra.api.qiita.api.GetArticleAPI
import hoge.hogehoge.myapplication.infra.api.qiita.api.GetArticlesAPI
import hoge.hogehoge.myapplication.infra.api.qiita.model.ArticleInAPI
import io.reactivex.Single

interface QiitaRemoteStore {
    fun fetchArticle(request: GetArticleAPI.Request): Single<ArticleInAPI>

    fun fetchArticles(request: GetArticlesAPI.Request): Single<List<ArticleInAPI>>
}
