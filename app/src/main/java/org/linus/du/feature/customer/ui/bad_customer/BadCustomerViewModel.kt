package org.linus.du.feature.customer.ui.bad_customer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import org.linus.core.utils.toast.Toaster
import org.linus.du.feature.customer.domain.repository.CustomerRepository
import javax.inject.Inject

@HiltViewModel
class BadCustomerViewModel @Inject constructor(
    private val repository: CustomerRepository,
    private val toaster: Toaster
) : ViewModel() {
    val badCustomers = Pager(
        PagingConfig(pageSize = 50, prefetchDistance = 150)
    ) {
        repository.getBadCustomers()
    }.flow.flowOn(Dispatchers.IO).cachedIn(viewModelScope)
}