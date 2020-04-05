package hoge.hogehoge.infra.store

import hoge.hogehoge.infra.database.dao.ArticleDao
import hoge.hogehoge.infra.database.entity.ArticleInDB
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class QiitaLocalStoreImpl @Inject constructor(
    private val articleDao: ArticleDao
) : QiitaLocalStore {
    override fun fetchArticle(articleId: String): Maybe<ArticleInDB> {
        return articleDao.fetchArticle(articleId).subscribeOn(Schedulers.io())
    }

    override fun fetchArticles(): Single<List<ArticleInDB>> {
        return articleDao.fetchAll().subscribeOn(Schedulers.io())
    }

    override fun upsertArticles(vararg articlesInDB: ArticleInDB): Completable {
        return articleDao.upsert(*articlesInDB).subscribeOn(Schedulers.io())
    }

    override fun deleteArticle(articlesInDB: ArticleInDB): Completable {
        return articleDao.delete(articlesInDB).subscribeOn(Schedulers.io())
    }
}
