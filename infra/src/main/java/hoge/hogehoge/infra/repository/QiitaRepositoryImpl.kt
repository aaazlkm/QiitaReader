package hoge.hogehoge.infra.repository

import hoge.hogehoge.infra.api.qiita.api.GetArticleAPI
import hoge.hogehoge.infra.api.qiita.api.GetArticlesAPI
import hoge.hogehoge.infra.api.qiita.model.ArticleInAPI
import hoge.hogehoge.infra.database.entity.ArticleInDB
import hoge.hogehoge.infra.store.QiitaLocalStore
import hoge.hogehoge.infra.store.QiitaRemoteStore
import io.reactivex.Completable
import io.reactivex.Maybe
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

    override fun fetchSavedArticle(articleId: String): Maybe<ArticleInDB> {
        return qiitaLocalStore.fetchArticle(articleId)
    }

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
