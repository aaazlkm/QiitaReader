package hoge.hogehoge.myapplication.domain.usecase

import hoge.hogehoge.myapplication.domain.entity.Article
import hoge.hogehoge.myapplication.domain.result.Result
import io.reactivex.Observable

interface QiitaUseCase {
    fun fetchArticle(articleId: String): Observable<Result<Article.Remote>>

    /**
     * トレンドの記事を取得する
     *
     * 条件
     *  - 1週間前から今日までの記事
     *  - ストック数が100以上
     *
     * @param page ページ
     * @param perPage ページ毎に読み込む数
     * @return
     */
    fun fetchTrendArticles(page: Int, perPage: Int): Observable<Result<List<Article.Remote>>>

    /**
     * 人気の記事を取得する
     *
     * 条件
     *  - 10年前から今日までの記事
     *  - ストック数が5000以上
     *
     * @param page ページ
     * @param perPage ページ毎に読み込む数
     * @return
     */
    fun fetchPopularArticles(page: Int, perPage: Int): Observable<Result<List<Article.Remote>>>

    /**
     * 最新の記事を取得する
     *
     * @param page ページ
     * @param perPage ページ毎に読み込む数
     * @return
     */
    fun fetchTimelineArticles(page: Int, perPage: Int): Observable<Result<List<Article.Remote>>>

    fun fetchSavedArticles(): Observable<Result<List<Article.Local>>>

    fun upsertSavedArticles(vararg articles: Article): Observable<Result<Boolean>>

    fun deleteSavedArticle(article: Article.Local): Observable<Result<Boolean>>
}
