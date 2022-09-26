package org.linus.core.data.db.dao

import androidx.room.Insert
import androidx.room.OnConflictStrategy

abstract class BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(entities: List<T>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract fun insert(entity: T): Long
}