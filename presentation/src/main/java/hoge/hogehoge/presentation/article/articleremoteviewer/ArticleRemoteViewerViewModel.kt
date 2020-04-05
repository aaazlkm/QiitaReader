package hoge.hogehoge.presentation.article.articleremoteviewer

import androidx.lifecycle.ViewModel
import hoge.hogehoge.domain.entity.Article
import hoge.hogehoge.domain.result.Result
import hoge.hogehoge.domain.usecase.QiitaUseCase
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.processors.BehaviorProcessor
import io.reactivex.rxkotlin.Flowables
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class ArticleRemoteViewerViewModel @Inject constructor(
    private val qiitaUseCase: hoge.hogehoge.domain.usecase.QiitaUseCase
) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    //region event

    private val eventOfGettingArticleProcessor = BehaviorProcessor.createDefault<hoge.hogehoge.domain.result.Result<hoge.hogehoge.domain.entity.Article.Remote>>(hoge.hogehoge.domain.result.Result.onReady())
    val eventOfGettingArticle: Flowable<hoge.hogehoge.domain.result.Result<hoge.hogehoge.domain.entity.Article.Remote>> = eventOfGettingArticleProcessor.observeOn(AndroidSchedulers.mainThread())

    private val eventOfSavingArticleProcessor = BehaviorProcessor.createDefault<hoge.hogehoge.domain.result.Result<Boolean>>(hoge.hogehoge.domain.result.Result.onReady())
    val eventOfSavingArticle: Flowable<hoge.hogehoge.domain.result.Result<Boolean>> = eventOfSavingArticleProcessor.observeOn(AndroidSchedulers.mainThread())

    //endregion

    //region value

    val isLoading: Flowable<Boolean> = Flowables.combineLatest(
        eventOfGettingArticle.map { it is hoge.hogehoge.domain.result.Result.Loading },
        eventOfSavingArticle.map { it is hoge.hogehoge.domain.result.Result.Loading }
    ) { p1, p2 -> p1 || p2 }.observeOn(AndroidSchedulers.mainThread())

    private val articleProcessor = BehaviorProcessor.create<hoge.hogehoge.domain.entity.Article>()
    val article: Flowable<hoge.hogehoge.domain.entity.Article> = articleProcessor.observeOn(AndroidSchedulers.mainThread())
    val articleValue: hoge.hogehoge.domain.entity.Article?
        get() = articleProcessor.value

    //endregion

    //region ViewModel override methods

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    //endregion

    fun fetchArticle(articleId: String) {
        qiitaUseCase.fetchArticle(articleId)
            .doOnNext { if (it is hoge.hogehoge.domain.result.Result.Success) articleProcessor.onNext(it.value) }
            .subscribe { eventOfGettingArticleProcessor.onNext(it) }
            .addTo(compositeDisposable)
    }

    fun saveArticle(article: hoge.hogehoge.domain.entity.Article) {
        qiitaUseCase.upsertSavedArticles(article)
            .subscribe { eventOfSavingArticleProcessor.onNext(it) }
            .addTo(compositeDisposable)
    }
}
