package hoge.hogehoge.domain.result

import androidx.annotation.CheckResult
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

@CheckResult
fun <T> Flowable<T>.toResult(): Flowable<Result<T>> {
    return compose { item ->
        item
            .map { Result.success(it) }
            .onErrorReturn { e -> Result.failure(e) }
            .startWith(Result.loading())
    }
}

@CheckResult
fun <T> Observable<T>.toResult(): Observable<Result<T>> {
    return compose { item ->
        item
            .map { Result.success(it) }
            .onErrorReturn { e -> Result.failure(e) }
            .startWith(Result.loading())
    }
}

@CheckResult
fun <T> Single<T>.toResult(): Observable<Result<T>> {
    return toObservable().toResult()
}

@CheckResult
fun <T> Maybe<T>.toResult(): Observable<Result<T>> {
    return toSingle().toResult()
}

@CheckResult
fun <T> Maybe<T>.toResult(defaultValue: T): Observable<Result<T>> {
    return toSingle(defaultValue).toResult()
}

@CheckResult
fun Completable.toResult(): Observable<Result<Boolean>> {
    return this.toSingleDefault(true).toResult()
}
