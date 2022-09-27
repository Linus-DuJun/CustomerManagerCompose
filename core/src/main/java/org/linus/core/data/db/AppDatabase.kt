package org.linus.core.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import org.linus.core.data.db.dao.CustomerDao
import org.linus.core.data.db.dao.RecordDao
import org.linus.core.data.db.entities.Customer
import org.linus.core.data.db.entities.Record

@Database(entities = [Customer::class, Record::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun customerDao(): CustomerDao
    abstract fun recordDao(): RecordDao
}