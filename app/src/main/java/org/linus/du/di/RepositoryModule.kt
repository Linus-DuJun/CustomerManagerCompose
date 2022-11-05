package org.linus.du.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import org.linus.du.feature.customer.data.repository.CustomerRepositoryImpl
import org.linus.du.feature.customer.data.repository.RecordRepositoryImpl
import org.linus.du.feature.customer.data.repository.ReturnVisitRepositoryImpl
import org.linus.du.feature.customer.domain.repository.CustomerRepository
import org.linus.du.feature.customer.domain.repository.RecordRepository
import org.linus.du.feature.customer.domain.repository.ReturnVisitRepository

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    @ViewModelScoped
    abstract fun bindsAddCustomerRepository(repositoryImpl: CustomerRepositoryImpl): CustomerRepository

    @Binds
    @ViewModelScoped
    abstract fun bindsRecordRepository(repositoryImpl: RecordRepositoryImpl): RecordRepository

    @Binds
    @ViewModelScoped
    abstract fun bindReturnVisitRepository(repositoryImpl: ReturnVisitRepositoryImpl): ReturnVisitRepository
}