package hoge.hogehoge.presentation.article.articleremote

import hoge.hogehoge.domain.entity.Article

sealed class ArticleResult {
    abstract val articles: List<hoge.hogehoge.domain.entity.Article.Remote>

    data class Cache(val position: Int, override val articles: List<hoge.hogehoge.domain.entity.Article.Remote>) : ArticleResult()

    data class New(override val articles: List<hoge.hogehoge.domain.entity.Article.Remote>) : ArticleResult()
}
