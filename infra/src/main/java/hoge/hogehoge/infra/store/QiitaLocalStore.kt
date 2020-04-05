package hoge.hogehoge.infra.store

import hoge.hogehoge.infra.database.entity.ArticleInDB
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

interface QiitaLocalStore {
    fun fetchArticle(articleId: String): Maybe<ArticleInDB>

    fun fetchArticles(): Single<List<ArticleInDB>>

    fun upsertArticles(vararg articlesInDB: ArticleInDB): Completable

    fun deleteArticle(articlesInDB: ArticleInDB): Completable
}
