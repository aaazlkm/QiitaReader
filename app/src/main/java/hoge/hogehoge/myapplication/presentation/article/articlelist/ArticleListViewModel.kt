package hoge.hogehoge.myapplication.presentation.article.articlelist

import androidx.lifecycle.ViewModel
import hoge.hogehoge.myapplication.domain.entity.Article
import hoge.hogehoge.myapplication.domain.result.Result
import hoge.hogehoge.myapplication.domain.usecase.QiitaUseCase
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.processors.BehaviorProcessor
import io.reactivex.rxkotlin.addTo
import javax.inject.Inject

class ArticleListViewModel @Inject constructor(
    private val qiitaUseCase: QiitaUseCase
) : ViewModel() {
    private val compositeDisposable = CompositeDisposable()

    private val articlesProcessor = BehaviorProcessor.createDefault<Result<List<Article.Remote>>>(Result.onReady())
    val articles: Flowable<Result<List<Article.Remote>>> = articlesProcessor.observeOn(AndroidSchedulers.mainThread())

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun fetchArticles() {
        qiitaUseCase.fetchArticles("1", "30")
            .subscribe { result -> articlesProcessor.onNext(result) }
            .addTo(compositeDisposable)
    }
}
