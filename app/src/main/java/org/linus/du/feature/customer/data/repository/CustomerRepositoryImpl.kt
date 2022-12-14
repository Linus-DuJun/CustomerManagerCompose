package org.linus.du.feature.customer.data.repository

import androidx.paging.PagingSource
import kotlinx.coroutines.flow.Flow
import org.linus.core.data.db.dao.CustomerDao
import org.linus.core.data.db.entities.Customer
import org.linus.du.feature.customer.domain.repository.CustomerRepository
import javax.inject.Inject

class CustomerRepositoryImpl @Inject constructor(
    private val customerDao: CustomerDao
) : CustomerRepository {

    override suspend fun getCustomerCount(): Int {
        return customerDao.getCustomerCount()
    }

    override suspend fun getCustomersByOffset(offset: Int): List<Customer> {
        return customerDao.getCustomersByOffset(offset * 300)
    }

    override suspend fun addCustomer(customer: Customer) {
        customerDao.insert(customer)
    }

    override suspend fun addCustomers(customers: List<Customer>) {
        customerDao.insert(customers)
    }

    override suspend fun updateCustomerLevel(phone: String, level: Int) {
        customerDao.updateCustomerLevel(phone = phone, level = level)
    }

    override suspend fun getCustomer(id: String): Flow<Customer> {
        return customerDao.getCustomerById(id)
    }

    override suspend fun getCustomerByName(name: String): Flow<List<Customer>> {
        return customerDao.getCustomerByName(name)
    }

    override suspend fun deleteCustomer(customer: Customer) {
        customerDao.delete(customer = customer)
    }

    override fun getSuperCustomers(): PagingSource<Int, Customer> =
        customerDao.getSuperCustomers()

    override fun getNormalCustomers(): PagingSource<Int, Customer> =
        customerDao.getNormalCustomers()

    override fun getBadCustomers(): PagingSource<Int, Customer> =
        customerDao.getBadCustomers()
}