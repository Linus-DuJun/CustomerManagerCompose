package org.linus.core.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import org.linus.core.data.db.entities.Subject

@Dao
abstract class SubjectDao : BaseDao<Subject>() {

    @Query("SELECT subject, time, description, human_readable_time FROM subject WHERE customer_phone = :phone ORDER BY time ASC LIMIT 100")
    abstract fun getSubjectsByUser(phone: String): Flow<List<SimpleSubject>>

    @Delete
    abstract fun delete(record: Subject)
}

data class SimpleSubject(
    val subject: String,
    val time: Long,
    val description: String,
    val human_readable_time: String
)