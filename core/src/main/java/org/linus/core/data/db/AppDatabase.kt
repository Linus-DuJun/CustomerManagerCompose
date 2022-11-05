package org.linus.core.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import org.linus.core.data.db.dao.CustomerDao
import org.linus.core.data.db.dao.ReturnVisitDao
import org.linus.core.data.db.dao.SubjectDao
import org.linus.core.data.db.entities.Customer
import org.linus.core.data.db.entities.ReturnVisitEntity
import org.linus.core.data.db.entities.Subject

@Database(entities = [Customer::class, Subject::class, ReturnVisitEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun customerDao(): CustomerDao
    abstract fun subjectDao(): SubjectDao
    abstract fun returnVisitDao(): ReturnVisitDao
}