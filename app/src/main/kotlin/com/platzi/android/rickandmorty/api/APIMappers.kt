package com.platzi.android.rickandmorty.api

import com.platzi.android.rickandmorty.domain.Character
import com.platzi.android.rickandmorty.domain.Episode
import com.platzi.android.rickandmorty.domain.Location
import com.platzi.android.rickandmorty.domain.Origin

fun CharacterResponseServer.toCharacterDomainList(): List<Character> = results.map {
<<<<<<< HEAD
    // pasar de server a domain
    it.run {
=======
    it.run{
>>>>>>> feat/step_19/extra_use_cases_module
        Character(
            id,
            name,
            image,
            gender,
            species,
            status,
            origin.toOriginDomain(),
            location.toLocationDomain(),
            episodeList.map { episode -> "$episode/" }
        )
    }
}

fun OriginServer.toOriginDomain() = Origin(
<<<<<<< HEAD
=======
    name,
    url
)

fun LocationServer.toLocationDomain() = Location(
>>>>>>> feat/step_19/extra_use_cases_module
    name,
    url
)

<<<<<<< HEAD
fun LocationServer.toLocationDomain() = Location(
    name,
    url
=======
fun EpisodeServer.toEpisodeDomain() = Episode(
    id,
    name
>>>>>>> feat/step_19/extra_use_cases_module
)

fun EpisodeServer.toEpisodeDomain() = Episode(
    id,
    name
)