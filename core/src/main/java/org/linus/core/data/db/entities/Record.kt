package org.linus.core.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Subject(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val userId: Int,
    val subject: String,
    val description: String,
    val timeStamp: Long,
)