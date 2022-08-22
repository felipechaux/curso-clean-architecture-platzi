package com.platzi.android.rickandmorty.database

import com.platzi.android.rickandmorty.data.LocalCharacterDataSource
import com.platzi.android.rickandmorty.domain.Character
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

<<<<<<< HEAD
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
=======
class CharacterRoomDataSource(
    database: CharacterDatabase
): LocalCharacterDataSource {

    //region Fields

    private val characterDao by lazy { database.characterDao() }

    //endregion

    //region

    override fun getAllFavoriteCharacters(): Flowable<List<Character>> = characterDao
        .getAllFavoriteCharacters()
        .map(List<CharacterEntity>::toCharacterDomainList)
        .onErrorReturn { emptyList() }
        .subscribeOn(Schedulers.io())

    override fun getFavoriteCharacterStatus(characterId: Int): Maybe<Boolean> {
        return characterDao.getCharacterById(characterId)
>>>>>>> feat/step_19/extra_use_cases_module
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
<<<<<<< HEAD
                if (isEmpty) {
                    characterDao.insertCharacter(characterEntity)
                } else {
=======
                if(isEmpty){
                    characterDao.insertCharacter(characterEntity)
                }else{
>>>>>>> feat/step_19/extra_use_cases_module
                    characterDao.deleteCharacter(characterEntity)
                }
                Maybe.just(isEmpty)
            }
            .observeOn(AndroidSchedulers.mainThread())
<<<<<<< HEAD
    }
=======
            .subscribeOn(Schedulers.io())
    }

    //endregion

>>>>>>> feat/step_19/extra_use_cases_module
}
