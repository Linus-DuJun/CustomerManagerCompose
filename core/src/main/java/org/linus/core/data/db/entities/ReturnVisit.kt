package org.linus.core.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ReturnVisit(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "customer_name") val customerName: String,
    @ColumnInfo(name = "customer_phone") val customerPhone: String,
    @ColumnInfo(name = "record_title") val recordTitle: String,
    @ColumnInfo(name = "rv_title") val rvTitle: String,
    @ColumnInfo(name = "rv_time") val rvTime: Long,
    @ColumnInfo(name = "human_readable_time") val humanReadableTime: String
)