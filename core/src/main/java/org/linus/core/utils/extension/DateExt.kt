package org.linus.core.utils.extension

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*


fun dateFor(ld: LocalDate): Date {
    return Date.from(
        ld.atStartOfDay()
            .atZone(ZoneId.systemDefault())
            .toInstant()
    )
}

fun localDateFor(date: Date): LocalDate {
    return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
}

fun getReadableDateByTime(timeStamp: Long): String {
    val time = LocalDateTime.ofInstant(Instant.ofEpochMilli(timeStamp), TimeZone.getDefault().toZoneId())
    val localDate = time.toLocalDate()
    val month = localDate.month.value
    return "${month}月${localDate.dayOfMonth}日"
}

fun getReadableBirthday(timeStamp: Long): String {
    val time = LocalDateTime.ofInstant(Instant.ofEpochMilli(timeStamp), TimeZone.getDefault().toZoneId())
    val localDate = time.toLocalDate()
    val month = localDate.month.value
    val year = localDate.year
    return "${year}年${month}月${localDate.dayOfMonth}日"
}

fun getHumanReadableDate(): String {
    val c = Calendar.getInstance()
    return "${c.get(Calendar.YEAR)} 年 ${c.get(Calendar.MONTH) + 1} 月 ${c.get(Calendar.DAY_OF_MONTH)}"
}