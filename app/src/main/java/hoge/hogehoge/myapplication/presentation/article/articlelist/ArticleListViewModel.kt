package hoge.hogehoge.myapplication.presentation.article.articlelist

import androidx.lifecycle.ViewModel
import hoge.hogehoge.myapplication.domain.entity.Article
import hoge.hogehoge.myapplication.domain.result.Result
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.processors.BehaviorProcessor
import io.reactivex.processors.PublishProcessor
import io.reactivex.rxkotlin.addTo

abstract class ArticleListViewModel : ViewModel() {
    companion object {
        const val INITIAL_LAST_READ_PAGE = 1
        /** 1ページ当たりに読み込む記事の数 */
        const val COUNT_ARTICLE_PER_PAGE = 15
    }

    private val compositeDisposable = CompositeDisposable()

    //region articles cache

    /** 最後に読み込んだQiitaのページ */
    private var lastReadPage = INITIAL_LAST_READ_PAGE

    private var lastScrollPosition: Int = 0

    private var articlesCache = mutableListOf<Article.Remote>()

    //endregion

    //region event

    private val eventOfGettingArticlesProcessor = BehaviorProcessor.createDefault<Result<List<Article.Remote>>>(Result.onReady())
    val eventOfGettingArticles: Flowable<Result<List<Article.Remote>>> = eventOfGettingArticlesProcessor.observeOn(AndroidSchedulers.mainThread())

    //endregion

    //region value

    val isLoading: Flowable<Boolean> = eventOfGettingArticles.map { it is Result.Loading }.observeOn(AndroidSchedulers.mainThread())

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

    fun fetchArticlesOrCache() {
        // ローディング中の場合、リクエストを無視する
        if (eventOfGettingArticlesProcessor.value is Result.Loading) return

        if (articlesCache.isNotEmpty()) {
            articlesProcessor.onNext(ArticleResult.Cache(lastScrollPosition, articlesCache))
        } else {
            fetchArticles()
        }
    }

    fun fetchArticles() {
        // ローディング中の場合、リクエストを無視する
        if (eventOfGettingArticlesProcessor.value is Result.Loading) return

        getArticleDataSource(lastReadPage, COUNT_ARTICLE_PER_PAGE)
            .doOnNext { if (it is Result.Success) articlesProcessor.onNext(ArticleResult.New(it.value)) }
            .subscribe { result -> eventOfGettingArticlesProcessor.onNext(result) }
            .addTo(compositeDisposable)

        lastReadPage++
    }

    fun saveState(lastScrollPosition: Int, articles: List<Article.Remote>) {
        this.lastScrollPosition = lastScrollPosition
        this.articlesCache.clear()
        this.articlesCache.addAll(articles)
    }

    fun resetState() {
        this.lastReadPage = INITIAL_LAST_READ_PAGE
        this.lastScrollPosition = 0
        this.articlesCache.clear()
    }
}
