package hoge.hogehoge.myapplication.domain.usecase

import hoge.hogehoge.myapplication.domain.entity.Article
import hoge.hogehoge.myapplication.domain.result.Result
import io.reactivex.Observable

interface QiitaUseCase {
    fun fetchArticle(articleId: String): Observable<Result<Article.Remote>>

    fun fetchArticles(page: Int, perPage: Int): Observable<Result<List<Article.Remote>>>

    fun fetchSavedArticles(): Observable<Result<List<Article.Local>>>

    fun upsertSavedArticles(vararg articles: Article): Observable<Result<Boolean>>

    fun deleteSavedArticle(article: Article): Observable<Result<Boolean>>
}
