package hoge.hogehoge.core.utility

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import java.net.HttpURLConnection
import java.net.URL

object DownloadUtility {
    fun loadBitmapFromUrl(url: String, timeout: Int = 30 * 1000): Single<Bitmap> {
        return Single.create<Bitmap> { emitter ->
            runCatching {
                val url = URL(url)
                val connection = (url.openConnection() as HttpURLConnection).apply {
                    this.connectTimeout = timeout
                }
                connection.connect()
                BitmapFactory.decodeStream(connection.inputStream)
            }.fold(
                onSuccess = {
                    emitter.onSuccess(it)
                },
                onFailure = {
                    emitter.onError(it)
                }
            )
        }.subscribeOn(Schedulers.newThread())
    }
}
