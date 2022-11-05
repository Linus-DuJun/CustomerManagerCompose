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