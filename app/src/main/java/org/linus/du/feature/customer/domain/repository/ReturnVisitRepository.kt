package org.linus.du.feature.customer.domain.repository

import kotlinx.coroutines.flow.Flow
import org.linus.core.data.db.entities.ReturnVisitEntity

interface ReturnVisitRepository {
    suspend fun getReturnVisitCount(): Int
    suspend fun getReturnVisitByOffset(offset: Int): List<ReturnVisitEntity>
    suspend fun addReturnVisitItems(items: List<ReturnVisitEntity>)
    suspend fun getReturnVisitItems(): Flow<List<ReturnVisitEntity>>
    suspend fun deleteReturnVisit(rv: ReturnVisitEntity)
    suspend fun deleteReturnVisitsByCustomer(id: String)
}