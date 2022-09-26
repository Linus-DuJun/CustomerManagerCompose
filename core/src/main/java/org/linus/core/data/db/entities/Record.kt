package org.linus.core.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Record(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val userId: Int,
    val subject: String,
    val timeStamp: Long,
    val type: Int, // 1 就诊  2 添加预约记录
)