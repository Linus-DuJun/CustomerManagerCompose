package org.linus.du.feature.customer.data.repository

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
}