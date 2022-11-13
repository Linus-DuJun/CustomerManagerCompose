package org.linus.du.feature.customer.data.repository

import kotlinx.coroutines.flow.Flow
import org.linus.core.data.db.dao.ReturnVisitDao
import org.linus.core.data.db.entities.ReturnVisitEntity
import org.linus.du.feature.customer.domain.repository.ReturnVisitRepository
import javax.inject.Inject

class ReturnVisitRepositoryImpl @Inject constructor(
    private val dao: ReturnVisitDao
): ReturnVisitRepository {
    override suspend fun addReturnVisitItems(items: List<ReturnVisitEntity>) {
        dao.insert(entities = items)
    }

    override suspend fun getReturnVisitItems(): Flow<List<ReturnVisitEntity>> {
        return dao.getReturnVisit(System.currentTimeMillis() - 24 * 60 * 60 * 1000)
    }

    override suspend fun deleteReturnVisit(rv: ReturnVisitEntity) {
        dao.delete(rv)
    }
}