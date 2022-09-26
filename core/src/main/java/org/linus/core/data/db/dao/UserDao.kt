package org.linus.core.data.db.dao

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.linus.core.data.db.entities.Customer

@Dao
abstract class UserDao : BaseDao<Customer>() {
    @Query("SELECT * FROM customer")
    abstract fun getAll(): Flow<List<Customer>>

    @Query("SELECT * FROM customer WHERE tel = :id")
    abstract fun getCustomerById(id: Int)


    @Delete
    abstract fun delete(customer: Customer)
}