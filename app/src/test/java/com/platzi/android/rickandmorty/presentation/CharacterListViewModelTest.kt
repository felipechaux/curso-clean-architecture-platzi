package com.platzi.android.rickandmorty.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.verify
import com.platzi.android.rickandmorty.domain.Character
import com.platzi.android.rickandmorty.domain.Location
import com.platzi.android.rickandmorty.domain.Origin
import com.platzi.android.rickandmorty.usecases.GetAllCharactersUseCase
import io.reactivex.Single
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

// prueba para viewmodel
@RunWith(MockitoJUnitRunner::class)
class CharacterListViewModelTest {

    @get: Rule
    val rxSchedulerRule = RxSchedulerRule()

    @get: Rule
    val rule: TestRule = InstantTaskExecutorRule()

    // verificar eventos
    @Mock
    lateinit var getAllCharactersUseCase: GetAllCharactersUseCase

    @Mock
    lateinit var eventObserver: Observer<Event<CharacterListViewModel.CharacterListNagivation>>

    private lateinit var characterListViewModel: CharacterListViewModel

    // ejecutar metodo antes de cada prueba
    @Before
    fun setup() {
        characterListViewModel = CharacterListViewModel(getAllCharactersUseCase)
    }

    // prueba
    @Test
    fun `onGetAllCharacters should return an expected success list of characters`() {
        // parametros
        // mock character
        val expectedResult = listOf(mockedCharacter.copy(id = 1))
        // dado comportamiento, siemore devolvera single con valor esperado -> character
        given(getAllCharactersUseCase.invoke(any())).willReturn(Single.just(expectedResult))

        characterListViewModel.events.observeForever(eventObserver)

        // ejecutar funcion de prueba
        characterListViewModel.onGetAllCharacters()

        // verificar
        verify(eventObserver).onChanged(
            Event(
                CharacterListViewModel.CharacterListNagivation.ShowCharacterList(
                    expectedResult
                )
            )
        )
    }
}

val mockedOrigin = Origin(
    "",
    ""
)

val mockedLocation = Location(
    "",
    ""
)

val mockedCharacter = Character(
    0,
    "",
    null,
    "",
    "",
    "",
    mockedOrigin,
    mockedLocation,
    listOf()
)
