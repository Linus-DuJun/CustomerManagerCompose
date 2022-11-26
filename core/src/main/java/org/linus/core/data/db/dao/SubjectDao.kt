package org.linus.core.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.linus.core.data.db.entities.Customer
import org.linus.core.data.db.entities.Subject

@Dao
abstract class SubjectDao : BaseDao<Subject>() {

    @Query("SELECT COUNT(*) FROM subject")
    abstract fun getRecordCount(): Int

    @Query("SELECT * FROM subject LIMIT 300 OFFSET :offset")
    abstract fun getRecordByOffset(offset: Int): List<Subject>

    @Query("SELECT * FROM subject WHERE customer_phone = :phone ORDER BY time ASC LIMIT 500")
    abstract fun getSubjectsByUser(phone: String): Flow<List<Subject>>

    @Delete
    abstract fun delete(record: Subject)
}

