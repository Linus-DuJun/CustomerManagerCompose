package org.linus.core.data.db.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.linus.core.data.db.entities.Customer

@Dao
abstract class CustomerDao : BaseDao<Customer>() {
    @Query("SELECT * FROM customer")
    abstract fun getAll(): Flow<List<Customer>>

    @Query("SELECT * FROM customer WHERE id = :id")
    abstract fun getCustomerById(id: Int): Customer


    @Delete
    abstract fun delete(customer: Customer)
}