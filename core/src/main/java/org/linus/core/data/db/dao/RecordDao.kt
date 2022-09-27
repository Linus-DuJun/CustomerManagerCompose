package org.linus.core.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import org.linus.core.data.db.entities.Customer
import org.linus.core.data.db.entities.Record

@Dao
abstract class RecordDao : BaseDao<Record>() {
    @Delete
    abstract fun delete(record: Record)
}