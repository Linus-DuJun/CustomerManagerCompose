package org.linus.core.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import org.linus.core.data.db.entities.ReturnVisit

@Dao
abstract class ReturnVisitDao: BaseDao<ReturnVisit>() {
    @Delete
    abstract fun delete(record: ReturnVisit)
}