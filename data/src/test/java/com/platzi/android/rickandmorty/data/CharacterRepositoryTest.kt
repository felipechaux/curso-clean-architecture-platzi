package com.platzi.android.data

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.given
import com.platzi.android.rickandmorty.data.CharacterRepository
import com.platzi.android.rickandmorty.data.LocalCharacterDataSource
import com.platzi.android.rickandmorty.data.RemoteCharacterDataSource
import com.platzi.android.rickandmorty.domain.Character
import com.platzi.android.rickandmorty.domain.Location
import com.platzi.android.rickandmorty.domain.Origin
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CharacterRepositoryTest {

    @Mock
    private lateinit var localCharacterDataSource: LocalCharacterDataSource

    @Mock
    private lateinit var remoteCharacterDataSource: RemoteCharacterDataSource

    private lateinit var characterRepository: CharacterRepository

    @Before
    fun setUp() {
        characterRepository =
            CharacterRepository(remoteCharacterDataSource, localCharacterDataSource)
    }

    @Test
    fun `getAllCharacters should return an expected success list of characters given a page number`() {
        val expectedResult = listOf(mockedCharacter.copy(id = 1))
        given(remoteCharacterDataSource.getAllCharacters(any())).willReturn(Single.just(expectedResult))

        // ejecutar
        characterRepository.getAllCharacters(page = 1)
            .test()
            // evaluar que se complete
            .assertComplete()
            // evaluar que no hay errores
            .assertNoErrors()
            // devolver solo un valor
            .assertValueCount(1)
            // finalmente obtener resultado esperado
            .assertValue(expectedResult)
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
