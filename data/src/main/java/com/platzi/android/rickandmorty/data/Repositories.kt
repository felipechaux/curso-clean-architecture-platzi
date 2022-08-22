package com.platzi.android.rickandmorty.data

import com.platzi.android.rickandmorty.domain.Character
<<<<<<< HEAD
=======
import com.platzi.android.rickandmorty.domain.Episode
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single
>>>>>>> feat/step_19/extra_use_cases_module

class CharacterRepository(
    private val remoteCharacterDataSource: RemoteCharacterDataSource,
    private val localCharacterDataSource: LocalCharacterDataSource
) {
<<<<<<< HEAD
    fun getAllCharacters(page: Int) = remoteCharacterDataSource.getAllCharacters(page)

    fun getAllFavoriteCharacters() = localCharacterDataSource.getAllFavoriteCharacters()

    fun getFavoriteCharacterStatus(id: Int) =
        localCharacterDataSource.getFavoriteCharacterStatus(id)

    fun updateFavoriteCharacterStatus(character: Character) =
        localCharacterDataSource.updateFavoriteCharacterStatus(character)
}

class EpisodeRepository(private val remoteEpisodeDataSource: RemoteEpisodeDataSource) {
    fun getEpisodesFromCharacter(episodeUrlList: List<String>) =
        remoteEpisodeDataSource.getEpisodesFromCharacter(episodeUrlList)
=======

    //region Public Methods

    fun getAllCharacters(page: Int): Single<List<Character>> =
        remoteCharacterDataSource.getAllCharacters(page)

    fun getAllFavoriteCharacters(): Flowable<List<Character>> =
        localCharacterDataSource.getAllFavoriteCharacters()

    fun getFavoriteCharacterStatus(characterId: Int): Maybe<Boolean> =
        localCharacterDataSource.getFavoriteCharacterStatus(characterId)

    fun updateFavoriteCharacterStatus(character: Character): Maybe<Boolean> =
        localCharacterDataSource.updateFavoriteCharacterStatus(character)

    //endregion
}

class EpisodeRepository(
    private val remoteEpisodeDataSource: RemoteEpisodeDataSource
) {

    //region Public Methods

    fun getEpisodeFromCharacter(episodeUrlList: List<String>): Single<List<Episode>> =
        remoteEpisodeDataSource.getEpisodeFromCharacter(episodeUrlList)

    //endregion
>>>>>>> feat/step_19/extra_use_cases_module
}
