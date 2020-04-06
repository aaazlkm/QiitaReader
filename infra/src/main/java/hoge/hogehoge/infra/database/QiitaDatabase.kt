package hoge.hogehoge.infra.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import hoge.hogehoge.infra.database.dao.ArticleDao
import hoge.hogehoge.infra.database.entity.ArticleInDB

@Database(
    entities = [ArticleInDB::class],
    version = QiitaDatabase.VERSION,
    exportSchema = false
)
abstract class QiitaDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao

    companion object {
        const val VERSION = 1
        const val DATABASE_NAME = "database-qiita"

        fun create(context: Context): QiitaDatabase {
            return Room.databaseBuilder(
                context,
                QiitaDatabase::class.java,
                DATABASE_NAME
            ).build()
        }
    }
}
