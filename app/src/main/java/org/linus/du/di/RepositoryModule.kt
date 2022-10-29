package org.linus.du.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import org.linus.du.feature.customer.data.repository.CustomerRepositoryImpl
import org.linus.du.feature.customer.domain.repository.CustomerRepository

@Module
@InstallIn(ViewModelComponent::class)
abstract class RepositoryModule {

    @Binds
    @ViewModelScoped
    abstract fun bindsAddCustomerRepository(repositoryImpl: CustomerRepositoryImpl): CustomerRepository
}