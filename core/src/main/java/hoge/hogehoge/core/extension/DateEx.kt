package hoge.hogehoge.core.extension

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

fun Date.format(format: String = "yyyy/MM/dd"): String {
    val formatter = SimpleDateFormat(format)
    return formatter.format(this)
}

fun Date.addYear(amount: Int): Date {
    val calendar = Calendar.getInstance().apply {
        time = this@addYear
        add(Calendar.YEAR, amount)
    }
    return calendar.time
}

fun Date.addMonth(amount: Int): Date {
    val calendar = Calendar.getInstance().apply {
        time = this@addMonth
        add(Calendar.MONTH, amount)
    }
    return calendar.time
}

fun Date.addDate(amount: Int): Date {
    val calendar = Calendar.getInstance().apply {
        time = this@addDate
        add(Calendar.DATE, amount)
    }
    return calendar.time
}
