package org.linus.du.feature.customer.ui.return_visit

import org.linus.core.data.db.entities.ReturnVisitEntity

data class ReturnVisitScreenStateHolder(
    val isLoading: Boolean = true,
    val items: List<ReturnVisitEntity> = emptyList()
)