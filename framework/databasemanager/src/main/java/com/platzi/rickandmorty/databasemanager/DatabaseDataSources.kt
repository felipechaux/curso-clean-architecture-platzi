package com.platzi.rickandmorty.databasemanager

import com.platzi.android.rickandmorty.data.LocalCharacterDataSource
import com.platzi.android.rickandmorty.domain.Character
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class CharacterRoomDataSource(database: CharacterDatabase) : LocalCharacterDataSource {

    private val characterDao by lazy { database.characterDao() }
    override fun getAllFavoriteCharacters(): Flowable<List<Character>> {
        return characterDao
            .getAllFavoriteCharacters()
            .map(List<CharacterEntity>::toCharacterDomainList)
            .onErrorReturn { emptyList() }
            .subscribeOn(Schedulers.io())
    }

    override fun getFavoriteCharacterStatus(id: Int): Maybe<Boolean> {
        return characterDao.getCharacterById(id)
            .isEmpty
            .flatMapMaybe { isEmpty ->
                Maybe.just(!isEmpty)
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
    }

    override fun updateFavoriteCharacterStatus(character: Character): Maybe<Boolean> {
        val characterEntity = character.toCharacterEntity()
        return characterDao.getCharacterById(characterEntity.id)
            .isEmpty
            .flatMapMaybe { isEmpty ->
                if (isEmpty) {
                    characterDao.insertCharacter(characterEntity)
                } else {
                    characterDao.deleteCharacter(characterEntity)
                }
                Maybe.just(isEmpty)
            }
            .observeOn(AndroidSchedulers.mainThread())
    }
}
