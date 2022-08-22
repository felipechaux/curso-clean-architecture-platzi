package com.platzi.android.rickandmorty.data

import com.platzi.android.rickandmorty.domain.Character
import com.platzi.android.rickandmorty.domain.Episode
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Single

interface RemoteCharacterDataSource {
    fun getAllCharacters(page: Int): Single<List<Character>>
}

interface LocalCharacterDataSource {
<<<<<<< HEAD
    fun getAllFavoriteCharacters(): Flowable<List<Character>>

    fun getFavoriteCharacterStatus(id: Int): Maybe<Boolean>
=======

    fun getAllFavoriteCharacters(): Flowable<List<Character>>

    fun getFavoriteCharacterStatus(characterId: Int): Maybe<Boolean>
>>>>>>> feat/step_19/extra_use_cases_module

    fun updateFavoriteCharacterStatus(character: Character): Maybe<Boolean>
}

interface RemoteEpisodeDataSource {
<<<<<<< HEAD
    fun getEpisodesFromCharacter(episodeUrlList: List<String>): Single<List<Episode>>
=======
    fun getEpisodeFromCharacter(episodeUrlList: List<String>): Single<List<Episode>>
>>>>>>> feat/step_19/extra_use_cases_module
}
