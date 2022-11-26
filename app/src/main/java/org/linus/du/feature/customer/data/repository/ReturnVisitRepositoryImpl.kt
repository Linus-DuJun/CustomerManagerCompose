package org.linus.du.feature.customer.data.repository

import kotlinx.coroutines.flow.Flow
import org.linus.core.data.db.dao.ReturnVisitDao
import org.linus.core.data.db.entities.ReturnVisitEntity
import org.linus.du.feature.customer.domain.repository.ReturnVisitRepository
import javax.inject.Inject

class ReturnVisitRepositoryImpl @Inject constructor(
    private val dao: ReturnVisitDao
): ReturnVisitRepository {
    override suspend fun getReturnVisitCount(): Int = dao.getReturnVisitCount()

    override suspend fun getReturnVisitByOffset(offset: Int): List<ReturnVisitEntity> =
        dao.getReturnVisitByOffset(offset)

    override suspend fun addReturnVisitItems(items: List<ReturnVisitEntity>) {
        dao.insert(entities = items)
    }

    override suspend fun getReturnVisitItems(): Flow<List<ReturnVisitEntity>> {
        return dao.getReturnVisit()
    }

    override suspend fun deleteReturnVisit(rv: ReturnVisitEntity) {
        dao.delete(rv)
    }

    override suspend fun deleteReturnVisitsByCustomer(id: String) {
        dao.deleteReturnVisitByCustomer(id)
    }
}