package org.linus.core.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ReturnVisit(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val subjectId: Int,
    val timeStamp: Long,
    val title: String,
    val description: String
)