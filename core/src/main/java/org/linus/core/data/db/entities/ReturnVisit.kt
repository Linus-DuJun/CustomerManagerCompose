package org.linus.core.data.db.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "return_visit")
data class ReturnVisitEntity(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "customer_name") val customerName: String,
    @ColumnInfo(name = "customer_phone") val customerPhone: String,
    @ColumnInfo(name="customer_type") val customerType: Int,
    @ColumnInfo(name = "record_id") val recordId: String,
    @ColumnInfo(name = "record_title") val recordTitle: String,
    @ColumnInfo(name = "rv_title") val rvTitle: String,
    @ColumnInfo(name = "rv_time") val rvTime: Long,
    val status: Int  // 1 未完成， 0 完成
)