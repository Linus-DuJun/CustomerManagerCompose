package org.linus.core.utils.extension

import java.time.LocalDate
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

fun getHumanReadableDate(): String {
    val c = Calendar.getInstance()
    return "${c.get(Calendar.YEAR)} 年 ${c.get(Calendar.MONTH) + 1} 月 ${c.get(Calendar.DAY_OF_MONTH)}"
}