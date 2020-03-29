package hoge.hogehoge.myapplication.infra.store

import hoge.hogehoge.myapplication.infra.api.qiita.api.GetArticleAPI
import hoge.hogehoge.myapplication.infra.api.qiita.model.ArticleInAPI
import io.reactivex.Single

interface QiitaRemoteStore {
    fun fetchArticles(request: GetArticleAPI.Request): Single<List<ArticleInAPI>>
}
