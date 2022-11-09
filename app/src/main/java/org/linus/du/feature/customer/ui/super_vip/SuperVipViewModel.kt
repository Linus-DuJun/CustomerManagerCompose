package org.linus.du.feature.customer.ui.super_vip

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOn
import org.linus.core.data.db.entities.Customer
import org.linus.core.utils.toast.Toaster
import org.linus.du.feature.customer.domain.repository.CustomerRepository
import javax.inject.Inject

@HiltViewModel
class SuperVipViewModel @Inject constructor(
    private val repository: CustomerRepository,
    private val toaster: Toaster
) : ViewModel() {

    val superCustomers: Flow<PagingData<Customer>> = Pager(
        PagingConfig(pageSize = 30, prefetchDistance = 150)
    ) {
        repository.getSuperCustomers()
    }.flow.flowOn(Dispatchers.IO).cachedIn(viewModelScope)

}