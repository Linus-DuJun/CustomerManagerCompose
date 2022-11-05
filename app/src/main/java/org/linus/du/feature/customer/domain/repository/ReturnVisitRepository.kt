package org.linus.du.feature.customer.domain.repository

import org.linus.core.data.db.entities.ReturnVisitEntity

interface ReturnVisitRepository {
    suspend fun addReturnVisitItems(items: List<ReturnVisitEntity>)
}