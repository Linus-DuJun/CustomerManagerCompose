package org.linus.du.feature.customer.domain.repository

import kotlinx.coroutines.flow.Flow
import org.linus.core.data.db.dao.SimpleSubject
import org.linus.core.data.db.entities.Subject

interface RecordRepository {
    suspend fun addRecord(record: Subject)

    suspend fun getRecordByCustomer(phone: String): Flow<List<SimpleSubject>>
}