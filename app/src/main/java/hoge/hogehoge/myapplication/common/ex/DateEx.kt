package hoge.hogehoge.myapplication.common.ex

import java.text.SimpleDateFormat
import java.util.Date

fun Date.format(format: String = "yyyy/MM/dd"): String {
    val formatter = SimpleDateFormat(format)
    return formatter.format(this)
}
