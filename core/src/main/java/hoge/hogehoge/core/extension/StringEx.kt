package hoge.hogehoge.core.extension

import java.text.SimpleDateFormat
import java.util.Date

/**
 * ISO8601形式でDateを返す
 * 失敗した場合nullを返す
 *
 * @return Date
 */
fun String.toDateInISO8601(): Date? {
    runCatching {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
        format.parse(this)
    }.fold(
        onSuccess = {
            return it
        },
        onFailure = {
            return null
        }
    )
}
