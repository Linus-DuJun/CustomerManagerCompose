package org.linus.du.di

import android.content.Context
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.linus.core.utils.toast.SimpleToaster
import org.linus.core.utils.toast.Toaster
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Singleton
    @Provides
    fun providesToaster(@ApplicationContext context: Context): Toaster = SimpleToaster(context)
}