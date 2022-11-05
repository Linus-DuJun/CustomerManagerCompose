package org.linus.du.feature.customer.data.repository

import org.linus.core.data.db.dao.SubjectDao
import org.linus.core.data.db.entities.Subject
import org.linus.du.feature.customer.domain.repository.RecordRepository
import javax.inject.Inject

class RecordRepositoryImpl @Inject constructor(
    private val subjectDao: SubjectDao
): RecordRepository {

    override suspend fun addRecord(record: Subject) {
        subjectDao.insert(record)
    }
}