package org.linus.du.feature.customer.data.repository

import kotlinx.coroutines.flow.Flow
import org.linus.core.data.db.dao.SubjectDao
import org.linus.core.data.db.entities.Customer
import org.linus.core.data.db.entities.Subject
import org.linus.du.feature.customer.domain.repository.RecordRepository
import javax.inject.Inject

class RecordRepositoryImpl @Inject constructor(
    private val subjectDao: SubjectDao
): RecordRepository {
    override suspend fun getRecordCount(): Int = subjectDao.getRecordCount()

    override suspend fun getRecordsByOffset(offset: Int): List<Subject> {
        return subjectDao.getRecordByOffset(offset)
    }

    override suspend fun addRecord(record: Subject) {
        subjectDao.insert(record)
    }

    override suspend fun getRecordByCustomer(phone: String): Flow<List<Subject>> =
        subjectDao.getSubjectsByUser(phone)
}