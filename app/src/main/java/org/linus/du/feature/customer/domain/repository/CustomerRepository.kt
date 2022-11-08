package org.linus.du.feature.customer.domain.repository

import androidx.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import org.linus.core.data.db.entities.Customer

interface CustomerRepository {

    suspend fun addCustomer(customer: Customer)

    suspend fun updateCustomerLevel(phone: String, level: Int)

    suspend fun getSuperCustomers(): Flow<PagingSource<Int, Customer>>

    suspend fun getNormalCustomers(): Flow<PagingSource<Int, Customer>>

    suspend fun getBadCustomers(): Flow<PagingSource<Int, Customer>>
}