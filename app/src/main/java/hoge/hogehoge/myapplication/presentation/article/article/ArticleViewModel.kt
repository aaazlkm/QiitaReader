package hoge.hogehoge.myapplication.presentation.article.article

import androidx.lifecycle.ViewModel
import hoge.hogehoge.myapplication.domain.entity.Article
import hoge.hogehoge.myapplication.domain.result.Result
import hoge.hogehoge.myapplication.domain.usecase.QiitaUseCase
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.processors.BehaviorProcessor
import io.reactivex.rxkotlin.Flowables
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class ArticleViewModel @Inject constructor(
    private val qiitaUseCase: QiitaUseCase
) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    //region event

    private val eventOfGettingArticleProcessor = BehaviorProcessor.createDefault<Result<Article.Remote>>(Result.onReady())
    val eventOfGettingArticle: Flowable<Result<Article.Remote>> = eventOfGettingArticleProcessor.observeOn(AndroidSchedulers.mainThread())

    private val eventOfSavingArticleProcessor = BehaviorProcessor.createDefault<Result<Boolean>>(Result.onReady())
    val eventOfSavingArticle: Flowable<Result<Boolean>> = eventOfSavingArticleProcessor.observeOn(AndroidSchedulers.mainThread())

    //endregion

    //region value

    val isLoading: Flowable<Boolean> = Flowables.combineLatest(
        eventOfGettingArticle.map { it is Result.Loading },
        eventOfSavingArticle.map { it is Result.Loading }
    ) { p1, p2 -> p1 || p2 }.observeOn(AndroidSchedulers.mainThread())

    private val articleProcessor = BehaviorProcessor.create<Article>()
    val article: Flowable<Article> = articleProcessor.observeOn(AndroidSchedulers.mainThread())
    val articleValue: Article?
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
            .doOnNext { if (it is Result.Success) articleProcessor.onNext(it.value) }
            .subscribe { eventOfGettingArticleProcessor.onNext(it) }
            .addTo(compositeDisposable)
    }

    fun saveArticle(article: Article) {
        qiitaUseCase.upsertSavedArticles(article)
            .subscribe { eventOfSavingArticleProcessor.onNext(it) }
            .addTo(compositeDisposable)
    }
}
