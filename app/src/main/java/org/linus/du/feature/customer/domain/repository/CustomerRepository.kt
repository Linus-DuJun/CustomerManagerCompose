package org.linus.du.feature.customer.domain.repository

import androidx.paging.PagingSource
import org.linus.core.data.db.entities.Customer

interface CustomerRepository {

    suspend fun addCustomer(customer: Customer)

    suspend fun updateCustomerLevel(phone: String, level: Int)

    fun getSuperCustomers(): PagingSource<Int, Customer>

    suspend fun getNormalCustomers(): PagingSource<Int, Customer>

    suspend fun getBadCustomers(): PagingSource<Int, Customer>
}