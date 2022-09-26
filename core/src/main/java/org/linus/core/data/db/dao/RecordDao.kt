package org.linus.core.data.db.dao

import androidx.room.Dao
import org.linus.core.data.db.entities.Record

@Dao
abstract class RecordDao : BaseDao<Record>() {
}