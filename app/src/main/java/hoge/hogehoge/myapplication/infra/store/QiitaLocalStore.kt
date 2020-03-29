package hoge.hogehoge.myapplication.infra.store

import hoge.hogehoge.myapplication.infra.database.entity.ArticleInDB
import io.reactivex.Completable
import io.reactivex.Single

interface QiitaLocalStore {

    fun fetchArticles(): Single<List<ArticleInDB>>

    fun upsertArticles(vararg articlesInDB: ArticleInDB): Completable

    fun deleteArticle(articlesInDB: ArticleInDB): Completable
}
