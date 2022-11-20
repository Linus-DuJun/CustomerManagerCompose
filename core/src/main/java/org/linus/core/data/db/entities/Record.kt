package org.linus.core.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subject")
data class Subject(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "customer_name") val customerName: String,
    @ColumnInfo(name = "customer_phone") val customerPhone: String,
    val subject: String,
    val time: Long
)