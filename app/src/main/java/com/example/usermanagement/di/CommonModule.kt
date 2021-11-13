package com.example.usermanagement.di

import com.example.usermanagement.interfaces.IDispatcher
import com.example.usermanagement.interfaces.UMDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object CommonModule {
    @Provides
    @Singleton
    fun provideDispatcher(): IDispatcher {
        return UMDispatcher()
    }

}