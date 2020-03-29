package hoge.hogehoge.myapplication.domain.usecase

import hoge.hogehoge.myapplication.common.ex.toResult
import hoge.hogehoge.myapplication.domain.entity.Article
import hoge.hogehoge.myapplication.domain.entity.Tag
import hoge.hogehoge.myapplication.domain.entity.User
import hoge.hogehoge.myapplication.domain.result.Result
import hoge.hogehoge.myapplication.infra.api.qiita.api.GetArticleAPI
import hoge.hogehoge.myapplication.infra.api.qiita.model.ArticleInAPI
import hoge.hogehoge.myapplication.infra.api.qiita.model.TagInAPI
import hoge.hogehoge.myapplication.infra.api.qiita.model.UserInAPI
import hoge.hogehoge.myapplication.infra.database.entity.ArticleInDB
import hoge.hogehoge.myapplication.infra.repository.QiitaRepository
import io.reactivex.Completable
import io.reactivex.Observable
import java.util.Date
import javax.inject.Inject

class QiitaUseCaseImpl @Inject constructor(
    private val qiitaRepository: QiitaRepository
) : QiitaUseCase {

    //region remote

    override fun fetchArticles(page: String, perPage: String): Observable<Result<List<Article.Remote>>> {
        val request = GetArticleAPI.Request(page, perPage)
        return qiitaRepository
            .fetchArticles(request)
            .map { convertArticleInAPI(it) }
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

    override fun upsertSavedArticles(vararg articles: Article): Completable {
        val articlesInDB =
            articles.map { article ->
                ArticleInDB(
                    article.articleId,
                    article.title,
                    article.bodyMarkDown,
                    Date().time
                )
            }.toTypedArray()

        return qiitaRepository.upsertSavedArticles(*articlesInDB)
    }

    override fun deleteSavedArticle(article: Article): Completable {
        if (article !is Article.Local) return Completable.complete()
        val articleInDB = ArticleInDB(
            article.articleId,
            article.title,
            article.bodyMarkDown,
            article.savedAt.time
        )

        return qiitaRepository.deleteSavedArticle(articleInDB)
    }

    //endregion

    //region convert methods

    private fun convertArticleInAPI(articlesInAPI: List<ArticleInAPI>): List<Article.Remote> {
        return articlesInAPI.mapNotNull {
            val articleId = it.id ?: return@mapNotNull null
            val title = it.title ?: return@mapNotNull null
            val bodyMarkDown = it.body ?: return@mapNotNull null
            val commentsCount = it.commentsCount ?: return@mapNotNull null
            val createdAt = it.createdAt ?: return@mapNotNull null
            val likesCount = it.likesCount ?: return@mapNotNull null
            val tags = convertTagsInAPI(it.tags)
            val url = it.url ?: return@mapNotNull null
            val user = it.user?.let { convertUserInAPI(it) } ?: return@mapNotNull null
            val pageViewsCount = it.pageViewsCount ?: return@mapNotNull null

            Article.Remote(
                articleId,
                title,
                bodyMarkDown,
                commentsCount,
                createdAt,
                likesCount,
                tags,
                url,
                user,
                pageViewsCount
            )
        }
    }

    private fun convertTagsInAPI(tagsInAPI: List<TagInAPI>): List<Tag.Remote> {
        return tagsInAPI.mapNotNull {
            val id = it.id ?: return@mapNotNull null
            val followersCount = it.followersCount ?: return@mapNotNull null
            val iconUrl = it.iconUrl ?: return@mapNotNull null
            val itemsCount = it.itemsCount ?: return@mapNotNull null

            Tag.Remote(
                id,
                followersCount,
                iconUrl,
                itemsCount
            )
        }
    }

    private fun convertUserInAPI(userInAPI: UserInAPI): User.Remote? {
        val id = userInAPI.id ?: return null
        val name = userInAPI.name ?: return null
        val description = userInAPI.description ?: return null
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
