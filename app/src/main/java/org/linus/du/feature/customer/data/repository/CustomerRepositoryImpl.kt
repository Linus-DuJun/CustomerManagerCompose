package org.linus.du.feature.customer.data.repository

import org.linus.core.data.db.dao.CustomerDao
import org.linus.du.feature.customer.domain.repository.CustomerRepository
import javax.inject.Inject

class CustomerRepositoryImpl @Inject constructor(
    private val customerDao: CustomerDao
) : CustomerRepository {
    // NEXT STEP : HILT INJECT ROOM DATABASE AND DAO
}