package com.mohi.examenma.di

import com.mohi.examenma.MainActivity
import com.mohi.examenma.model.repo.BaseEntitiesRepository
import com.mohi.examenma.model.repo.EntitiesRepository
import com.mohi.examenma.model.repo.localrepo.EntityDao
import com.mohi.examenma.model.repo.localrepo.LocalDatabase
import com.mohi.examenma.model.service.EntitiesService
import com.mohi.examenma.model.usecase.*
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(MainActivity.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun providesMAService(retrofit: Retrofit): EntitiesService {
        return retrofit.create(EntitiesService::class.java)
    }

    @Provides
    @Singleton
    fun providesEntityDao(): EntityDao {
        return LocalDatabase.getDatabase(MainActivity.bcontext).entityDao()
    }

    @Module
    @InstallIn(SingletonComponent::class)
    interface AppModuleInt {

        @Binds
        @Singleton
        fun provideMARepository(repo: BaseEntitiesRepository): EntitiesRepository

        @Binds
        @Singleton
        fun provideGetAllUseCase(uc: BaseGetEntitiesUseCase): GetEntitiesUseCase

        @Binds
        @Singleton
        fun provideAddUseCase(uc: BaseAddEntityUseCase): AddEntityUseCase

        @Binds
        @Singleton
        fun provideConfirmUseCase(uc: BaseCofirmUseCase): ConfirmUseCase

        @Binds
        @Singleton
        fun provideSyncUseCase(uc: BaseSyncUseCase): SyncUseCase
    }

}