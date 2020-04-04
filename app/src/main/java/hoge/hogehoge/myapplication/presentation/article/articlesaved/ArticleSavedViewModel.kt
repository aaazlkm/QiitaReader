package hoge.hogehoge.myapplication.presentation.article.articlesaved

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

class ArticleSavedViewModel @Inject constructor(
    private val qiitaUseCase: QiitaUseCase
) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    //region event

    private val eventOfGettingArticlesProcessor = BehaviorProcessor.createDefault<Result<List<Article.Local>>>(Result.onReady())
    val eventOfGettingArticles: Flowable<Result<List<Article.Local>>> = eventOfGettingArticlesProcessor.observeOn(AndroidSchedulers.mainThread())

    //endregion

    //region value

    val isLoading: Flowable<Boolean> = eventOfGettingArticles.map { it is Result.Loading }.observeOn(AndroidSchedulers.mainThread())

    private val articlesProcessor = PublishProcessor.create<List<Article.Local>>()
    val articles: Flowable<List<Article.Local>> = articlesProcessor.observeOn(AndroidSchedulers.mainThread())

    //endregion

    //region ViewModel override methods

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    //endregion

    fun fetchArticles() {
        // ローディング中の場合、リクエストを無視する
        if (eventOfGettingArticlesProcessor.value is Result.Loading) return

        qiitaUseCase.fetchSavedArticles()
            .doOnNext { if (it is Result.Success) articlesProcessor.onNext(it.value) }
            .subscribe { result -> eventOfGettingArticlesProcessor.onNext(result) }
            .addTo(compositeDisposable)
    }
}
