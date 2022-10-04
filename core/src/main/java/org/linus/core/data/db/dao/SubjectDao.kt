package org.linus.core.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import org.linus.core.data.db.entities.Subject

@Dao
abstract class SubjectDao : BaseDao<Subject>() {
    @Delete
    abstract fun delete(record: Subject)
}