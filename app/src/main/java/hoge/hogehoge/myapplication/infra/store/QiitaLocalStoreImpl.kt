package hoge.hogehoge.myapplication.infra.store

import hoge.hogehoge.myapplication.infra.database.dao.ArticleDao
import hoge.hogehoge.myapplication.infra.database.entity.ArticleInDB
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class QiitaLocalStoreImpl @Inject constructor(
    private val articleDao: ArticleDao
) : QiitaLocalStore {
    override fun fetchArticles(): Single<List<ArticleInDB>> {
        return articleDao.fetchAll()
    }

    override fun upsertArticles(vararg articlesInDB: ArticleInDB): Completable {
        return articleDao.upsert(*articlesInDB)
    }

    override fun deleteArticle(articlesInDB: ArticleInDB): Completable {
        return articleDao.delete(articlesInDB)
    }
}
