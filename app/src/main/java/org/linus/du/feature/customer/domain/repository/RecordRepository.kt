package org.linus.du.feature.customer.domain.repository

import org.linus.core.data.db.entities.Subject

interface RecordRepository {
    suspend fun addRecord(record: Subject)
}