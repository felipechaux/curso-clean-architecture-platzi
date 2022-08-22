package com.platzi.android.rickandmorty.usecases

import com.platzi.android.rickandmorty.data.CharacterRepository
import com.platzi.android.rickandmorty.domain.Character
import io.reactivex.Maybe

class UpdateFavoriteStatusUseCase(
    private val characterRepository: CharacterRepository
) {

    fun invoke(character: Character): Maybe<Boolean> {
        return characterRepository.updateFavoriteCharacterStatus(character)
    }
}
