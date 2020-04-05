package hoge.hogehoge.domain.usecase

import hoge.hogehoge.core.POPULAR_QUERY_CREATED_FROM_UNIT_YEAR
import hoge.hogehoge.core.POPULAR_QUERY_STOCKS
import hoge.hogehoge.core.TREND_QUERY_CREATED_FROM_UNIT_WEEK
import hoge.hogehoge.core.TREND_QUERY_STOCKS
import hoge.hogehoge.core.extension.addMonth
import hoge.hogehoge.core.extension.addYear
import hoge.hogehoge.core.extension.toDateInISO8601
import hoge.hogehoge.domain.entity.Article
import hoge.hogehoge.domain.entity.Tag
import hoge.hogehoge.domain.entity.User
import hoge.hogehoge.domain.exception.QiitaException
import hoge.hogehoge.domain.result.Result
import hoge.hogehoge.domain.result.toResult
import hoge.hogehoge.infra.api.qiita.api.GetArticleAPI
import hoge.hogehoge.infra.api.qiita.api.GetArticlesAPI
import hoge.hogehoge.infra.api.qiita.model.ArticleInAPI
import hoge.hogehoge.infra.api.qiita.model.TagInAPI
import hoge.hogehoge.infra.api.qiita.model.UserInAPI
import hoge.hogehoge.infra.database.entity.ArticleInDB
import hoge.hogehoge.infra.repository.QiitaRepository
import io.reactivex.Observable
import java.util.Date
import javax.inject.Inject

class QiitaUseCaseImpl @Inject constructor(
    private val qiitaRepository: QiitaRepository
) : QiitaUseCase {

    //region remote

    override fun fetchArticle(articleId: String): Observable<Result<Article.Remote>> {
        val request = GetArticleAPI.Request(articleId)
        return qiitaRepository
            .fetchArticle(request)
            .map { convertArticleInAPI(it) ?: throw QiitaException.FaildToConvertArticleResponse(articleId) }
            .toResult()
    }

    override fun fetchTrendArticles(page: Int, perPage: Int): Observable<Result<List<Article.Remote>>> {
        val query = GetArticlesAPI.createQuery(
            stocks = TREND_QUERY_STOCKS,
            createdFrom = Date().addMonth(-TREND_QUERY_CREATED_FROM_UNIT_WEEK)
        )
        val request = GetArticlesAPI.Request(page, perPage, query)
        return qiitaRepository
            .fetchArticles(request)
            .map { it.mapNotNull { convertArticleInAPI(it) } }
            .map { it.sortedByDescending { it.likesCount } }
            .toResult()
    }

    override fun fetchPopularArticles(page: Int, perPage: Int): Observable<Result<List<Article.Remote>>> {
        val query = GetArticlesAPI.createQuery(
            stocks = POPULAR_QUERY_STOCKS,
            createdFrom = Date().addYear(-POPULAR_QUERY_CREATED_FROM_UNIT_YEAR)
        )
        val request = GetArticlesAPI.Request(page, perPage, query)
        return qiitaRepository
            .fetchArticles(request)
            .map { it.mapNotNull { convertArticleInAPI(it) } }
            .map { it.sortedByDescending { it.likesCount } }
            .toResult()
    }

    override fun fetchTimelineArticles(page: Int, perPage: Int): Observable<Result<List<Article.Remote>>> {
        val request = GetArticlesAPI.Request(page, perPage)
        return qiitaRepository
            .fetchArticles(request)
            .map { it.mapNotNull { convertArticleInAPI(it) } }
            .toResult()
    }

    //endregion

    //region local

    override fun fetchSavedArticle(articleId: String): Observable<Result<Article.Saved>> {
        return qiitaRepository.fetchSavedArticle(articleId)
            .map { Article.Saved(it.articleId, it.title, it.bodyMarkDown, Date(it.savedAt)) }
            .toResult()
    }

    override fun fetchSavedArticles(): Observable<Result<List<Article.Saved>>> {
        return qiitaRepository
            .fetchSavedArticles()
            .map { articlesInDB -> articlesInDB.map { Article.Saved(it.articleId, it.title, it.bodyMarkDown, Date(it.savedAt)) } }
            .toResult()
    }

    override fun upsertSavedArticles(vararg articles: Article): Observable<Result<Boolean>> {
        val articlesInDB =
            articles.map { article ->
                ArticleInDB(
                    article.articleId,
                    article.title,
                    article.bodyMarkDown,
                    Date().time
                )
            }.toTypedArray()

        return qiitaRepository.upsertSavedArticles(*articlesInDB).toResult()
    }

    override fun deleteSavedArticle(article: Article.Saved): Observable<Result<Boolean>> {
        val articleInDB = ArticleInDB(
            article.articleId,
            article.title,
            article.bodyMarkDown,
            article.savedAt.time
        )

        return qiitaRepository.deleteSavedArticle(articleInDB).toResult()
    }

    //endregion

    //region convert methods

    private fun convertArticleInAPI(articlesInAPI: ArticleInAPI): Article.Remote? {
        val articleId = articlesInAPI.id ?: return null
        val title = articlesInAPI.title ?: return null
        // qiitaで取得するMarkdownの書式が一般的なものと異なるため調整している
        val bodyMarkDown = articlesInAPI.body?.let { "#+".toRegex().replace(it) { it.value + " " } } ?: return null
        val commentsCount = articlesInAPI.commentsCount ?: return null
        val createdAt = articlesInAPI.createdAt?.toDateInISO8601() ?: return null
        val likesCount = articlesInAPI.likesCount ?: return null
        val tags = convertTagsInAPI(articlesInAPI.tags)
        val url = articlesInAPI.url ?: return null
        val user = articlesInAPI.user?.let { convertUserInAPI(it) } ?: return null

        return Article.Remote(
            articleId,
            title,
            bodyMarkDown,
            commentsCount,
            createdAt,
            likesCount,
            tags,
            url,
            user
        )
    }

    private fun convertTagsInAPI(tagsInAPI: List<TagInAPI>): List<Tag.Remote> {
        return tagsInAPI.mapNotNull {
            val name = it.name ?: return@mapNotNull null
            val versions = it.versions ?: listOf()

            Tag.Remote(
                name,
                versions
            )
        }
    }

    private fun convertUserInAPI(userInAPI: UserInAPI): User.Remote? {
        val id = userInAPI.id ?: return null
        val name = userInAPI.name
        val description = userInAPI.description
        val profileImageUrl = userInAPI.profileImageUrl ?: return null

        return User.Remote(
            id,
            name,
            description,
            profileImageUrl
        )
    }

    //endregion
}
