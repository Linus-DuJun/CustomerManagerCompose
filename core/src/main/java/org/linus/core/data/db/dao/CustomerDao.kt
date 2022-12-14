package org.linus.core.data.db.dao

import androidx.paging.PagingSource
import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.linus.core.data.db.entities.Customer

@Dao
abstract class CustomerDao : BaseDao<Customer>() {
    @Query("SELECT * FROM customer")
    abstract fun getAll(): Flow<List<Customer>>

    @Query("SELECT COUNT(*) FROM customer")
    abstract fun getCustomerCount(): Int

    @Query("SELECT * FROM customer LIMIT 300 OFFSET :offset")
    abstract fun getCustomersByOffset(offset: Int): List<Customer>

    @Query("SELECT * FROM customer WHERE id = :id")
    abstract fun getCustomerById(id: String): Flow<Customer>

    @Query("SELECT * FROM customer WHERE name LIKE '%' || :name || '%' ")
    abstract fun getCustomerByName(name: String): Flow<List<Customer>>

    @Query("SELECT * FROM customer WHERE type = 3")
    abstract fun getSuperCustomers(): PagingSource<Int, Customer>

    @Query("SELECT * FROM customer WHERE type = 2")
    abstract fun getNormalCustomers(): PagingSource<Int, Customer>

    @Query("SELECT * FROM customer WHERE type = 1")
    abstract fun getBadCustomers(): PagingSource<Int, Customer>

    @Query("UPDATE customer SET type = :level WHERE id = :phone")
    abstract fun updateCustomerLevel(phone: String, level: Int)

    @Delete
    abstract fun delete(customer: Customer)
}