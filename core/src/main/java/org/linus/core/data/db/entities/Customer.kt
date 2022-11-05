package org.linus.core.data.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "customer")
data class Customer(
    @PrimaryKey val id: String, // tel number
    val name: String,
    val type: Int,  //1 普通  2 vip 3 super vip
)