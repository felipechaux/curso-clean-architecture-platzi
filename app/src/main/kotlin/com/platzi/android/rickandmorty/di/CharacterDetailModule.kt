package com.platzi.android.rickandmorty.di

import com.platzi.android.rickandmorty.domain.Character
import com.platzi.android.rickandmorty.presentation.CharacterDetailViewModel
import com.platzi.android.rickandmorty.usecases.GetEpisodeFromCharacterUseCase
import com.platzi.android.rickandmorty.usecases.GetFavoriteStatusUseCase
import com.platzi.android.rickandmorty.usecases.UpdateFavoriteStatusUseCase
import dagger.Module
import dagger.Provides
import dagger.Subcomponent

@Module
class CharacterDetailModule(
    private val character: Character?
) {
    @Provides
    fun characterListViewModelProvider(
        getEpisodeFromCharacterUseCase: GetEpisodeFromCharacterUseCase,
        getFavoriteStatusUseCase: GetFavoriteStatusUseCase,
        updateFavoriteStatusUseCase: UpdateFavoriteStatusUseCase
    ) =
        CharacterDetailViewModel(
            character,
            getEpisodeFromCharacterUseCase,
            getFavoriteStatusUseCase,
            updateFavoriteStatusUseCase
        )
}

@Subcomponent(modules = [(CharacterDetailModule::class)])
interface CharacterDetailComponent {
    val characterDetailViewModel: CharacterDetailViewModel
}
