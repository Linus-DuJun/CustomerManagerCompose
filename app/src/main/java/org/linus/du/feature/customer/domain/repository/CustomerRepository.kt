package org.linus.du.feature.customer.domain.repository

import androidx.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import org.linus.core.data.db.entities.Customer

interface CustomerRepository {
    suspend fun getCustomerCount(): Int
    suspend fun getCustomersByOffset(offset: Int): List<Customer>
    suspend fun addCustomer(customer: Customer)
    suspend fun addCustomers(customers: List<Customer>)
    suspend fun updateCustomerLevel(phone: String, level: Int)
    suspend fun getCustomer(id: String): Flow<Customer>
    suspend fun getCustomerByName(name: String): Flow<List<Customer>>
    suspend fun deleteCustomer(customer: Customer)
    fun getSuperCustomers(): PagingSource<Int, Customer>
    fun getNormalCustomers(): PagingSource<Int, Customer>
    fun getBadCustomers(): PagingSource<Int, Customer>
}