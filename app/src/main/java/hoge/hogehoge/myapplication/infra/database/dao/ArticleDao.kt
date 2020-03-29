package hoge.hogehoge.myapplication.infra.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import hoge.hogehoge.myapplication.infra.database.entity.ArticleInDB
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface ArticleDao {
    @Query("SELECT * FROM ${ArticleInDB.TABLE_NAME}")
    fun fetchAll(): Single<List<ArticleInDB>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(vararg articlesInDB: ArticleInDB): Completable

    @Delete
    fun delete(articlesInDB: ArticleInDB): Completable
}
