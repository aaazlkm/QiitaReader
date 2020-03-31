package hoge.hogehoge.myapplication.domain.usecase

import hoge.hogehoge.myapplication.domain.entity.Article
import hoge.hogehoge.myapplication.domain.result.Result
import io.reactivex.Completable
import io.reactivex.Observable

interface QiitaUseCase {
    fun fetchArticles(page: Int, perPage: Int): Observable<Result<List<Article.Remote>>>

    fun fetchSavedArticles(): Observable<Result<List<Article.Local>>>

    fun upsertSavedArticles(vararg articles: Article): Completable

    fun deleteSavedArticle(article: Article): Completable
}
