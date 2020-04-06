package hoge.hogehoge.presentation.article.articleremote

import androidx.lifecycle.ViewModel
import hoge.hogehoge.domain.entity.Article
import hoge.hogehoge.domain.result.Result
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.processors.BehaviorProcessor
import io.reactivex.processors.PublishProcessor
import io.reactivex.rxkotlin.addTo

abstract class ArticleRemoteViewModel : ViewModel() {
    companion object {
        const val INITIAL_LAST_READ_PAGE = 1
        /** 1ページ当たりに読み込む記事の数 */
        const val COUNT_ARTICLE_PER_PAGE = 15
    }

    data class ArticleCache(
        val lastScrollPosition: Int,
        val articlesCache: List<Article.Remote>
    )

    private val compositeDisposable = CompositeDisposable()

    /** 最後に読み込んだQiitaのページ */
    private var lastReadPage = INITIAL_LAST_READ_PAGE

    //region articles cache

    private var articleCache: ArticleCache? = null

    //endregion

    //region event

    private val eventOfGettingArticlesProcessor = BehaviorProcessor.createDefault<Result<List<Article.Remote>>>(Result.onReady())
    val eventOfGettingArticles: Flowable<Result<List<Article.Remote>>> = eventOfGettingArticlesProcessor.observeOn(AndroidSchedulers.mainThread())

    //endregion

    //region value

    private val isLoadingProcessor = BehaviorProcessor.createDefault<Boolean>(false)
    val isLoading: Flowable<Boolean> = isLoadingProcessor.observeOn(AndroidSchedulers.mainThread())

    private val articlesProcessor = PublishProcessor.create<ArticleResult>()
    val articles: Flowable<ArticleResult> = articlesProcessor.observeOn(AndroidSchedulers.mainThread())

    //endregion

    /**
     * Articleを取得できるデータソースを取得する
     *
     * @param page ページ
     * @param perPage ページ毎に読み込む数
     * @return Observable<Result<List<Article.Remote>>>
     */
    protected abstract fun getArticleDataSource(page: Int, perPage: Int): Observable<Result<List<Article.Remote>>>

    //region ViewModel override methods

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    //endregion

    fun fetchArticlesOrCache(needLoading: Boolean = true) {
        // ローディング中の場合、リクエストを無視する
        if (eventOfGettingArticlesProcessor.value is Result.Loading) return

        articleCache?.let {
            articlesProcessor.onNext(ArticleResult.Cache(it.lastScrollPosition, it.articlesCache))
        } ?: run {
            fetchArticles(needLoading)
        }
    }

    fun fetchArticles(needLoading: Boolean = true) {
        // ローディング中の場合、リクエストを無視する
        if (eventOfGettingArticlesProcessor.value is Result.Loading) return

        getArticleDataSource(lastReadPage, COUNT_ARTICLE_PER_PAGE)
            .subscribe { result ->
                eventOfGettingArticlesProcessor.onNext(result)
                if (result is Result.Success) {
                    articlesProcessor.onNext(ArticleResult.New(result.value))
                    lastReadPage++
                }
                if (needLoading) isLoadingProcessor.onNext(result is Result.Loading)
            }
            .addTo(compositeDisposable)
    }

    fun saveArticleCache(cache: ArticleCache) {
        this.articleCache = cache
    }

    fun clearArticleCache() {
        this.articleCache = null
    }

    fun resetState() {
        lastReadPage = INITIAL_LAST_READ_PAGE
        clearArticleCache()
    }
}
