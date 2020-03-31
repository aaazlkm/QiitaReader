package hoge.hogehoge.myapplication.presentation.article.articlelist

import androidx.lifecycle.ViewModel
import hoge.hogehoge.myapplication.domain.entity.Article
import hoge.hogehoge.myapplication.domain.result.Result
import hoge.hogehoge.myapplication.domain.usecase.QiitaUseCase
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.processors.BehaviorProcessor
import io.reactivex.processors.PublishProcessor
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class ArticleListViewModel @Inject constructor(
    private val qiitaUseCase: QiitaUseCase
) : ViewModel() {
    companion object {
        const val INITIAL_LAST_READ_PAGE = 1
        /** 1ページ当たりに読み込む記事の数 */
        const val COUNT_ARTICLE_PER_PAGE = 15
    }

    private val compositeDisposable = CompositeDisposable()

    /** 最後に読み込んだQiitaのページ */
    private var lastReadPage = INITIAL_LAST_READ_PAGE

    //region event

    private val articlesEventProcessor = BehaviorProcessor.createDefault<Result<List<Article.Remote>>>(Result.onReady())
    val articlesEvent: Flowable<Result<List<Article.Remote>>> = articlesEventProcessor.observeOn(AndroidSchedulers.mainThread())

    //endregion

    //region value

    val isLoading: Flowable<Boolean> = articlesEvent.map { it is Result.Loading }

    private val articlesProcessor = PublishProcessor.create<List<Article.Remote>>()
    val articles: Flowable<List<Article.Remote>> = articlesProcessor.observeOn(AndroidSchedulers.mainThread())

    //endregion

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun fetchArticlesIfNotLoading() {
        // ローディング中の場合、リクエストを無視する
        if (articlesEventProcessor.value is Result.Loading) return

        qiitaUseCase.fetchArticles(lastReadPage, COUNT_ARTICLE_PER_PAGE)
            .doOnNext { if (it is Result.Success) articlesProcessor.onNext(it.value) }
            .subscribe { result -> articlesEventProcessor.onNext(result) }
            .addTo(compositeDisposable)

        lastReadPage++
    }

    fun resetLastReadPage() {
        lastReadPage = INITIAL_LAST_READ_PAGE
    }
}
