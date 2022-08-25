package com.platzi.android.rickandmorty.usecases

import com.platzi.android.rickandmorty.data.CharacterRepository
import com.platzi.android.rickandmorty.data.EpisodeRepository
import dagger.Module
import dagger.Provides

@Module
class UseCaseModule {

    @Provides
    fun getAllCharacterUseCaseProvider(characterRepository: CharacterRepository) =
        GetAllCharactersUseCase(characterRepository)

    @Provides
    fun getAllFavoriteCharacterUseCaseProvider(characterRepository: CharacterRepository) =
        GetAllFavoriteCharacterUseCase(characterRepository)

    @Provides
    fun getEpisodeFromCharacterUseCase(episodeRepository: EpisodeRepository) =
        GetEpisodeFromCharacterUseCase(episodeRepository)

    @Provides
    fun getFavoriteStatusUseCase(characterRepository: CharacterRepository) =
        GetFavoriteStatusUseCase(characterRepository)

    @Provides
    fun updateFavoriteStatusUsesCase(characterRepository: CharacterRepository) =
        UpdateFavoriteStatusUseCase(characterRepository)
}
