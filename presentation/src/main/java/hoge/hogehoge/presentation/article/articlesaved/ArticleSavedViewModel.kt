package hoge.hogehoge.presentation.article.articlesaved

import androidx.lifecycle.ViewModel
import hoge.hogehoge.domain.entity.Article
import hoge.hogehoge.domain.result.Result
import hoge.hogehoge.domain.usecase.QiitaUseCase
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.processors.BehaviorProcessor
import io.reactivex.processors.PublishProcessor
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class ArticleSavedViewModel @Inject constructor(
    private val qiitaUseCase: hoge.hogehoge.domain.usecase.QiitaUseCase
) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    //region event

    private val eventOfGettingArticlesProcessor = BehaviorProcessor.createDefault<hoge.hogehoge.domain.result.Result<List<hoge.hogehoge.domain.entity.Article.Saved>>>(hoge.hogehoge.domain.result.Result.onReady())
    val eventOfGettingArticles: Flowable<hoge.hogehoge.domain.result.Result<List<hoge.hogehoge.domain.entity.Article.Saved>>> = eventOfGettingArticlesProcessor.observeOn(AndroidSchedulers.mainThread())

    //endregion

    //region value

    val isLoading: Flowable<Boolean> = eventOfGettingArticles.map { it is hoge.hogehoge.domain.result.Result.Loading }.observeOn(AndroidSchedulers.mainThread())

    private val articlesProcessor = PublishProcessor.create<List<hoge.hogehoge.domain.entity.Article.Saved>>()
    val articles: Flowable<List<hoge.hogehoge.domain.entity.Article.Saved>> = articlesProcessor.observeOn(AndroidSchedulers.mainThread())

    //endregion

    //region ViewModel override methods

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    //endregion

    fun fetchArticles() {
        // ローディング中の場合、リクエストを無視する
        if (eventOfGettingArticlesProcessor.value is hoge.hogehoge.domain.result.Result.Loading) return

        qiitaUseCase.fetchSavedArticles()
            .doOnNext { if (it is hoge.hogehoge.domain.result.Result.Success) articlesProcessor.onNext(it.value) }
            .subscribe { result -> eventOfGettingArticlesProcessor.onNext(result) }
            .addTo(compositeDisposable)
    }
}
