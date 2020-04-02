package hoge.hogehoge.myapplication.domain.usecase

import hoge.hogehoge.myapplication.common.ex.toDateInISO8601
import hoge.hogehoge.myapplication.common.ex.toResult
import hoge.hogehoge.myapplication.domain.entity.Article
import hoge.hogehoge.myapplication.domain.entity.Tag
import hoge.hogehoge.myapplication.domain.entity.User
import hoge.hogehoge.myapplication.domain.exception.QiitaException
import hoge.hogehoge.myapplication.domain.result.Result
import hoge.hogehoge.myapplication.infra.api.qiita.api.GetArticleAPI
import hoge.hogehoge.myapplication.infra.api.qiita.api.GetArticlesAPI
import hoge.hogehoge.myapplication.infra.api.qiita.model.ArticleInAPI
import hoge.hogehoge.myapplication.infra.api.qiita.model.TagInAPI
import hoge.hogehoge.myapplication.infra.api.qiita.model.UserInAPI
import hoge.hogehoge.myapplication.infra.database.entity.ArticleInDB
import hoge.hogehoge.myapplication.infra.repository.QiitaRepository
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

    override fun fetchArticles(page: Int, perPage: Int): Observable<Result<List<Article.Remote>>> {
        val request = GetArticlesAPI.Request(page, perPage)
        return qiitaRepository
            .fetchArticles(request)
            .map { it.mapNotNull { convertArticleInAPI(it) } }
            .toResult()
    }

    //endregion

    //region local

    override fun fetchSavedArticles(): Observable<Result<List<Article.Local>>> {
        return qiitaRepository
            .fetchSavedArticles()
            .map { articlesInDB -> articlesInDB.map { Article.Local(it.articleId, it.title, it.bodyMarkDown, Date(it.savedAt)) } }
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

    override fun deleteSavedArticle(article: Article): Observable<Result<Boolean>> {
        if (article !is Article.Local) return Observable.error(QiitaException.FaildToDeleteArticle)
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
