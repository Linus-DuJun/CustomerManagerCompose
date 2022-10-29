package org.linus.du.feature.customer.data.repository

import org.linus.core.data.db.dao.CustomerDao
import org.linus.core.data.db.entities.Customer
import org.linus.du.feature.customer.domain.repository.CustomerRepository
import javax.inject.Inject

class CustomerRepositoryImpl @Inject constructor(
    private val customerDao: CustomerDao
) : CustomerRepository {

    override fun addCustomer(customer: Customer) {

    }
}