package com.platzi.android.rickandmorty.di

import android.app.Application
import com.platzi.android.rickandmorty.data.RepositoryModule
import com.platzi.android.rickandmorty.requestmanager.ApiModule
import com.platzi.android.rickandmorty.usecases.UseCaseModule
import com.platzi.rickandmorty.databasemanager.DatabaseModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApiModule::class, DatabaseModule::class, RepositoryModule::class, UseCaseModule::class])
interface RickAndMortyPlatziComponent {
    fun inject(module: CharacterListModule): CharacterListComponent
    fun inject(module: FavoriteListModule): FavoriteListComponent

    @Component.Factory
    interface Favorite {
        fun create(@BindsInstance app: Application): RickAndMortyPlatziComponent
    }
}
