package org.linus.du.feature.customer.domain.repository

import org.linus.core.data.db.entities.Customer

interface CustomerRepository {

    fun addCustomer(customer: Customer)
}