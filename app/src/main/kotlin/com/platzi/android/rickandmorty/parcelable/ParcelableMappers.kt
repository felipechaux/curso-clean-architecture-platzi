package com.platzi.android.rickandmorty.parcelable

import android.os.Parcelable
import com.platzi.android.rickandmorty.domain.Character
import com.platzi.android.rickandmorty.domain.Location
import com.platzi.android.rickandmorty.domain.Origin
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CharacterParcelable(
    val id: Int,
    val name: String,
    val image: String?,
    val gender: String,
    val species: String,
    var status: String,
    var origin: OriginParcelable,
    var location: LocationParcelable,
    var episodeList: List<String>
) : Parcelable

@Parcelize
data class OriginParcelable(
    val name: String,
    val url: String
) : Parcelable

@Parcelize
data class LocationParcelable(
    val name: String,
    val url: String
) : Parcelable

fun Character.toCharacterParcelable() = CharacterParcelable(
    id, name, image, gender, species, status,
    origin.toOriginParcelable(),
    location.toLocationParcelable(),
    episodeList
)

fun Location.toLocationParcelable() = LocationParcelable(
    name,
    url
)

fun Origin.toOriginParcelable() = OriginParcelable(
    name,
    url
)

// cuando se resiva parcelable - este se debe cambiar para que sea de dominio
fun CharacterParcelable.toCharacterDomain() = Character(
    id, name, image, gender, species, status,
    origin.toOriginDomain(),
    location.toLocationDomain(),
    episodeList
)

fun LocationParcelable.toLocationDomain() = Location(
    name,
    url
)

fun OriginParcelable.toOriginDomain() = Origin(
    name,
    url
)