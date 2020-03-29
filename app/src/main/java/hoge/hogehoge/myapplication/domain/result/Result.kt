package hoge.hogehoge.myapplication.domain.result

sealed class Result<T> {
    companion object {
        fun <T> success(value: T): Result<T> = Success(value)

        fun <T> failure(error: Throwable): Result<T> = Failure(error)

        fun <T> loading(): Result<T> = Loading()

        fun <T> onReady(): Result<T> = OnReady()
    }

    data class Success<T>(val value: T) : Result<T>()
    data class Failure<T>(val error: Throwable) : Result<T>()
    class Loading<T> : Result<T>()
    class OnReady<T> : Result<T>()
}

fun <T> Result<T>.successOr(fallback: T): T {
    return (this as? Result.Success<T>)?.value ?: fallback
}

inline fun <R, T> Result<T>.fold(
    onSuccess: (value: T) -> R,
    onFailure: (exception: Throwable) -> R
) {
    when (this) {
        is Result.Success -> onSuccess(value)
        is Result.Failure -> onFailure(error)
    }
}
