package hoge.hogehoge.myapplication.infra.repository

import hoge.hogehoge.myapplication.infra.api.qiita.api.GetArticleAPI
import hoge.hogehoge.myapplication.infra.api.qiita.api.GetArticlesAPI
import hoge.hogehoge.myapplication.infra.api.qiita.model.ArticleInAPI
import hoge.hogehoge.myapplication.infra.database.entity.ArticleInDB
import hoge.hogehoge.myapplication.infra.store.QiitaLocalStore
import hoge.hogehoge.myapplication.infra.store.QiitaRemoteStore
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class QiitaRepositoryImpl @Inject constructor(
    private val qiitaLocalStore: QiitaLocalStore,
    private val qiitaRemoteStore: QiitaRemoteStore
) : QiitaRepository {

    //region remote

    override fun fetchArticle(request: GetArticleAPI.Request): Single<ArticleInAPI> {
        return qiitaRemoteStore.fetchArticle(request)
    }

    override fun fetchArticles(request: GetArticlesAPI.Request): Single<List<ArticleInAPI>> {
        return qiitaRemoteStore.fetchArticles(request)
    }

    //endregion

    //region local

    override fun fetchSavedArticles(): Single<List<ArticleInDB>> {
        return qiitaLocalStore.fetchArticles()
    }

    override fun upsertSavedArticles(vararg articles: ArticleInDB): Completable {
        return qiitaLocalStore.upsertArticles(*articles)
    }

    override fun deleteSavedArticle(article: ArticleInDB): Completable {
        return qiitaLocalStore.deleteArticle(article)
    }

    //endregion
}
