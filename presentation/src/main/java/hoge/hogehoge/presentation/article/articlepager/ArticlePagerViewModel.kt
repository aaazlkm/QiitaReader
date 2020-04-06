package hoge.hogehoge.presentation.article.articlepager

import androidx.lifecycle.ViewModel
import hoge.hogehoge.domain.result.Result
import hoge.hogehoge.domain.usecase.QiitaUseCase
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.processors.PublishProcessor
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject
import timber.log.Timber

class ArticlePagerViewModel @Inject constructor(
    private val qiitaUseCase: QiitaUseCase
) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    private val numberOfUnreadArticleProcessor = PublishProcessor.create<Int>()
    val numberOfUnreadArticle: Flowable<Int> = numberOfUnreadArticleProcessor.observeOn(AndroidSchedulers.mainThread())

    //region ViewModel override methods

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    //endregion

    fun fetchNumberOfUnreadArticle() {
        qiitaUseCase.fetchSavedArticles()
            .subscribe { result ->
                when (result) {
                    is Result.Success -> {
                        val numberOfUnreadArticle = result.value.filter { !it.alreadyRead }.size
                        numberOfUnreadArticleProcessor.onNext(numberOfUnreadArticle)
                    }
                    is Result.Failure -> {
                        // 画面表示に必要なデータではないのでエラーの場合は無視する
                        Timber.e("failed to fetchNumberOfUnreadArticle: ${result.error} ")
                    }
                }
            }
            .addTo(compositeDisposable)
    }
}
