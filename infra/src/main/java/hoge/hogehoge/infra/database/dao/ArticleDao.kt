package hoge.hogehoge.infra.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import hoge.hogehoge.infra.database.entity.ArticleInDB
import io.reactivex.Completable
import io.reactivex.Maybe
import io.reactivex.Single

@Dao
interface ArticleDao {
    @Query("SELECT * FROM ${ArticleInDB.TABLE_NAME} WHERE ${ArticleInDB.TABLE_COLUMN_ARTICLE_ID} = :articleId")
    fun fetchArticle(articleId: String): Maybe<ArticleInDB>

    @Query("SELECT * FROM ${ArticleInDB.TABLE_NAME}")
    fun fetchAll(): Single<List<ArticleInDB>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(vararg articlesInDB: ArticleInDB): Completable

    @Delete
    fun delete(articlesInDB: ArticleInDB): Completable
}
