package hoge.hogehoge.domain.entity

import android.graphics.Bitmap
import java.util.Date

sealed class Article {
    abstract val articleId: String
    abstract val title: String
    abstract val bodyMarkDown: String

    data class Remote(
        override val articleId: String,
        override val title: String,
        override val bodyMarkDown: String,
        val commentsCount: Int,
        val createdAt: Date,
        val likesCount: Int,
        val tags: List<Tag.Remote>,
        val url: String,
        val user: User.Remote
    ) : Article() {
        // 読み込んだIconを保持する
        var userIcon: Bitmap? = null
    }

    data class Saved(
        override val articleId: String,
        override val title: String,
        override val bodyMarkDown: String,
        val savedAt: Date
    ) : Article()
}
