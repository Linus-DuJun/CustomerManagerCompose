package org.linus.du.feature.customer.data.repository

import androidx.paging.PagingSource
import org.linus.core.data.db.dao.CustomerDao
import org.linus.core.data.db.entities.Customer
import org.linus.du.feature.customer.domain.repository.CustomerRepository
import javax.inject.Inject

class CustomerRepositoryImpl @Inject constructor(
    private val customerDao: CustomerDao
) : CustomerRepository {

    override suspend fun addCustomer(customer: Customer) {
        customerDao.insert(customer)
    }

    override suspend fun addCustomers(customers: List<Customer>) {
        customerDao.insert(customers)
    }

    override suspend fun updateCustomerLevel(phone: String, level: Int) {
        customerDao.updateCustomerLevel(phone = phone, level = level)
    }

    override fun getSuperCustomers(): PagingSource<Int, Customer> =
        customerDao.getSuperCustomers()

    override fun getNormalCustomers(): PagingSource<Int, Customer> =
        customerDao.getNormalCustomers()

    override fun getBadCustomers(): PagingSource<Int, Customer> =
        customerDao.getBadCustomers()
}