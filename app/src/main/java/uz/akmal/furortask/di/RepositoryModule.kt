package uz.akmal.furortask.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.akmal.furortask.model.ApiService
import uz.akmal.furortask.model.MainRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun getRepository(service: ApiService): MainRepository = MainRepository(service)
}