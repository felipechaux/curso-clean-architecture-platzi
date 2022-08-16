package com.platzi.android.rickandmorty.database

import com.platzi.android.rickandmorty.domain.Character
import com.platzi.android.rickandmorty.domain.Location
import com.platzi.android.rickandmorty.domain.Origin

fun List<CharacterEntity>.toCharacterDomainList() = map(CharacterEntity::toCharacterDomain)

fun CharacterEntity.toCharacterDomain() = Character(
    id,
    name,
    image,
    gender,
    species,
    status,
    origin.toOriginDomain(),
    location.toLocationDomain(),
    episodeList
)

fun OriginEntity.toOriginDomain() = Origin(
    originName,
    originUrl
)

fun LocationEntity.toLocationDomain() = Location(
    locationName,
    locationUrl
)

// / de dominio a entity (base de datos)
fun Character.toCharacterEntity() = CharacterEntity(
    id,
    name,
    image,
    gender,
    species,
    status,
    origin.toOriginEntity(),
    location.toLocationEntity(),
    episodeList
)

fun Origin.toOriginEntity() = OriginEntity(
    name,
    url
)

fun Location.toLocationEntity() = LocationEntity(
    name,
    url
)
