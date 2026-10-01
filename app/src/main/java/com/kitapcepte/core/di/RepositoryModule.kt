package com.kitapcepte.core.di

import com.kitapcepte.data.repository.AuthRepositoryImpl
import com.kitapcepte.data.repository.SessionRepositoryImpl
import com.kitapcepte.domain.repository.AuthRepository
import com.kitapcepte.domain.repository.SessionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds
    @Singleton
    abstract fun bindSessionRepository(impl: SessionRepositoryImpl): SessionRepository
}
