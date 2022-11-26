package org.linus.du.feature.customer.domain.repository

import kotlinx.coroutines.flow.Flow
import org.linus.core.data.db.entities.Customer
import org.linus.core.data.db.entities.Subject

interface RecordRepository {
    suspend fun getRecordCount(): Int
    suspend fun getRecordsByOffset(offset: Int): List<Subject>
    suspend fun addRecord(record: Subject)
    suspend fun getRecordByCustomer(phone: String): Flow<List<Subject>>
}