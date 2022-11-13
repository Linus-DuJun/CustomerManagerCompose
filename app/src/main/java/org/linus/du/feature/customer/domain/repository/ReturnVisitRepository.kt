package org.linus.du.feature.customer.domain.repository

import kotlinx.coroutines.flow.Flow
import org.linus.core.data.db.entities.ReturnVisitEntity
import org.linus.du.feature.customer.ui.add_customer.ReturnVisit

interface ReturnVisitRepository {
    suspend fun addReturnVisitItems(items: List<ReturnVisitEntity>)
    suspend fun getReturnVisitItems(): Flow<List<ReturnVisitEntity>>
    suspend fun deleteReturnVisit(rv: ReturnVisitEntity)
}