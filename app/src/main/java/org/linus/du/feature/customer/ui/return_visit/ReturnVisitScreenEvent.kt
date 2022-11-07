package org.linus.du.feature.customer.ui.return_visit

import org.linus.core.data.db.entities.ReturnVisitEntity

sealed class ReturnVisitScreenEvent {
    data class ItemsLoadedEvent(val items: List<ReturnVisitEntity>) : ReturnVisitScreenEvent()
}