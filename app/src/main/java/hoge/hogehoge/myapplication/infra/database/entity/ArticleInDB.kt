package hoge.hogehoge.myapplication.infra.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = ArticleInDB.TABLE_NAME)
data class ArticleInDB(
    @PrimaryKey @ColumnInfo(name = TABLE_COLUMN_ARTICLE_ID) val articleId: String,
    @ColumnInfo(name = TABLE_COLUMN_Title) val title: String,
    @ColumnInfo(name = TABLE_COLUMN_BODY_MARK_DOWN) val bodyMarkDown: String,
    @ColumnInfo(name = TABLE_COLUMN_SAVED_AT) val savedAt: Long
) {
    companion object {
        const val TABLE_NAME = "table_article"

        const val TABLE_COLUMN_ARTICLE_ID = "article_id"
        const val TABLE_COLUMN_Title = "title"
        const val TABLE_COLUMN_BODY_MARK_DOWN = "body_mark_down"
        const val TABLE_COLUMN_SAVED_AT = "saved_at"
    }
}
