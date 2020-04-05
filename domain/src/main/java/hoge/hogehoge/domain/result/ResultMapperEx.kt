package hoge.hogehoge.domain.result

import androidx.annotation.CheckResult
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

@CheckResult
fun <T> Flowable<T>.toResult(): Flowable<hoge.hogehoge.domain.result.Result<T>> {
    return compose { item ->
        item
            .map { hoge.hogehoge.domain.result.Result.success(it) }
            .onErrorReturn { e -> hoge.hogehoge.domain.result.Result.failure(e) }
            .startWith(hoge.hogehoge.domain.result.Result.loading())
    }
}

@CheckResult
fun <T> Observable<T>.toResult(): Observable<hoge.hogehoge.domain.result.Result<T>> {
    return compose { item ->
        item
            .map { hoge.hogehoge.domain.result.Result.success(it) }
            .onErrorReturn { e -> hoge.hogehoge.domain.result.Result.failure(e) }
            .startWith(hoge.hogehoge.domain.result.Result.loading())
    }
}

@CheckResult
fun <T> Single<T>.toResult(): Observable<hoge.hogehoge.domain.result.Result<T>> {
    return toObservable().toResult()
}

@CheckResult
fun <T> Maybe<T>.toResult(): Observable<hoge.hogehoge.domain.result.Result<T>> {
    return toSingle().toResult()
}

@CheckResult
fun <T> Maybe<T>.toResult(defaultValue: T): Observable<hoge.hogehoge.domain.result.Result<T>> {
    return toSingle(defaultValue).toResult()
}

@CheckResult
fun Completable.toResult(): Observable<hoge.hogehoge.domain.result.Result<Boolean>> {
    return this.toSingleDefault(true).toResult()
}
