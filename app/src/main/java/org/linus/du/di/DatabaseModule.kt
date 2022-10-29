package org.linus.du.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.linus.core.data.db.AppDatabase
import org.linus.core.data.db.dao.CustomerDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "CustomerManager").build()
    }

    @Provides
    @Singleton
    fun provideCustomerDao(database: AppDatabase): CustomerDao {
        return database.customerDao()
    }

    @Provides
    @Singleton
    fun provideSubjectDao(database: AppDatabase) = database.subjectDao()

    @Provides
    @Singleton
    fun provideReturnVisitDao(database: AppDatabase) = database.returnVisitDao()
}