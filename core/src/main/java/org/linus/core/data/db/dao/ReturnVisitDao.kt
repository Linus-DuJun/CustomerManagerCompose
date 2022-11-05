package org.linus.core.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import org.linus.core.data.db.entities.ReturnVisitEntity

@Dao
abstract class ReturnVisitDao: BaseDao<ReturnVisitEntity>() {
    @Delete
    abstract fun delete(record: ReturnVisitEntity)
}