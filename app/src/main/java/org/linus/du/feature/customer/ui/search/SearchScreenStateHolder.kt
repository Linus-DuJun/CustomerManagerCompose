package org.linus.du.feature.customer.ui.search

import org.linus.core.data.db.entities.Customer
import org.linus.core.utils.extension.empty

data class SearchScreenStateHolder(
    val keyword: String = String.empty(),
    val isError: Boolean = false,
    val result: List<Customer> = listOf()
)