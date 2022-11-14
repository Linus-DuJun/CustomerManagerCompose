package org.linus.du.feature.customer.ui.search

import org.linus.core.data.db.entities.Customer

sealed class SearchScreenEvent {
    object SearchConfirmEvent: SearchScreenEvent()
    object NoSearchKeyWordEvent: SearchScreenEvent()
    data class SearchKeyInputEvent(val keyword: String): SearchScreenEvent()
    data class SearchResultEvent(val customers: List<Customer>): SearchScreenEvent()
}