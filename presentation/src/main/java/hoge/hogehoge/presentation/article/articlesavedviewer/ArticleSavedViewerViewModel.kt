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
    private val qiitaUseCase: hoge.hogehoge.domain.usecase.QiitaUseCase
) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    //region event

    private val eventOfGettingArticleProcessor = BehaviorProcessor.createDefault<hoge.hogehoge.domain.result.Result<hoge.hogehoge.domain.entity.Article.Saved>>(hoge.hogehoge.domain.result.Result.onReady())
    val eventOfGettingArticle: Flowable<hoge.hogehoge.domain.result.Result<hoge.hogehoge.domain.entity.Article.Saved>> = eventOfGettingArticleProcessor.observeOn(AndroidSchedulers.mainThread())

    private val eventOfDeletingArticleProcessor = BehaviorProcessor.createDefault<hoge.hogehoge.domain.result.Result<Boolean>>(hoge.hogehoge.domain.result.Result.onReady())
    val eventOfDeletingArticle: Flowable<hoge.hogehoge.domain.result.Result<Boolean>> = eventOfDeletingArticleProcessor.observeOn(AndroidSchedulers.mainThread())

    //endregion

    //region value

    val isLoading: Flowable<Boolean> = eventOfGettingArticle.map { it is hoge.hogehoge.domain.result.Result.Loading }.observeOn(AndroidSchedulers.mainThread())

    private val articleProcessor = BehaviorProcessor.create<hoge.hogehoge.domain.entity.Article.Saved>()
    val article: Flowable<hoge.hogehoge.domain.entity.Article.Saved> = articleProcessor.observeOn(AndroidSchedulers.mainThread())
    val articleValue: hoge.hogehoge.domain.entity.Article.Saved?
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
            .doOnNext { if (it is hoge.hogehoge.domain.result.Result.Success) articleProcessor.onNext(it.value) }
            .subscribe { eventOfGettingArticleProcessor.onNext(it) }
            .addTo(compositeDisposable)
    }

    fun deleteArticle(article: hoge.hogehoge.domain.entity.Article.Saved) {
        qiitaUseCase.deleteSavedArticle(article)
            .subscribe { eventOfDeletingArticleProcessor.onNext(it) }
            .addTo(compositeDisposable)
    }
}
