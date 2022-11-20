package org.linus.core.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.linus.core.data.db.entities.ReturnVisitEntity

@Dao
abstract class ReturnVisitDao: BaseDao<ReturnVisitEntity>() {

    @Query(
        "SELECT * FROM return_visit WHERE status = 1 ORDER BY rv_time ASC LIMIT 100"
    )
    abstract fun getReturnVisit(): Flow<List<ReturnVisitEntity>>


    @Delete
    abstract fun delete(record: ReturnVisitEntity)
}