package hoge.hogehoge.presentation.article.articlesavedviewer

import androidx.lifecycle.ViewModel
import hoge.hogehoge.domain.entity.Article
import hoge.hogehoge.domain.result.Result
import hoge.hogehoge.domain.usecase.QiitaUseCase
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.processors.BehaviorProcessor
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class ArticleSavedViewerViewModel @Inject constructor(
    private val qiitaUseCase: QiitaUseCase
) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    //region event

    private val eventOfGettingArticleProcessor = BehaviorProcessor.createDefault<Result<Article.Saved>>(Result.onReady())
    val eventOfGettingArticle: Flowable<Result<Article.Saved>> = eventOfGettingArticleProcessor.observeOn(AndroidSchedulers.mainThread())

    private val eventOfDeletingArticleProcessor = BehaviorProcessor.createDefault<Result<Boolean>>(Result.onReady())
    val eventOfDeletingArticle: Flowable<Result<Boolean>> = eventOfDeletingArticleProcessor.observeOn(AndroidSchedulers.mainThread())

    //endregion

    //region value

    val isLoading: Flowable<Boolean> = eventOfGettingArticle.map { it is Result.Loading }.observeOn(AndroidSchedulers.mainThread())

    private val articleProcessor = BehaviorProcessor.create<Article.Saved>()
    val article: Flowable<Article.Saved> = articleProcessor.observeOn(AndroidSchedulers.mainThread())
    val articleValue: Article.Saved?
        get() = articleProcessor.value

    //endregion

    //region ViewModel override methods

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    //endregion

    fun fetchArticle(articleId: String) {
        qiitaUseCase.fetchSavedArticle(articleId)
            .doOnNext { if (it is Result.Success) articleProcessor.onNext(it.value) }
            .subscribe { eventOfGettingArticleProcessor.onNext(it) }
            .addTo(compositeDisposable)
    }

    fun deleteArticle(article: Article.Saved) {
        qiitaUseCase.deleteSavedArticle(article)
            .subscribe { eventOfDeletingArticleProcessor.onNext(it) }
            .addTo(compositeDisposable)
    }
}
