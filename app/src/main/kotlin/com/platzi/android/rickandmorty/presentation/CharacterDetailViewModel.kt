package com.platzi.android.rickandmorty.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.platzi.android.rickandmorty.api.*
import com.platzi.android.rickandmorty.database.CharacterDao
import com.platzi.android.rickandmorty.database.CharacterEntity
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CharacterDetailViewModel(
    private val characterDao: CharacterDao,
    private val character: CharacterServer?,
    private val episodeRequest: EpisodeRequest
) : ViewModel() {

    private val disposable = CompositeDisposable()

    private val _events = MutableLiveData<Event<CharacterDetailNavigation>>()
    val events: LiveData<Event<CharacterDetailNavigation>> get() = _events

    private val _characterDetail = MutableLiveData<CharacterServer>()

    val characterDetail: MutableLiveData<CharacterServer>
        get() = _characterDetail

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    fun onValidateFavoriteCharacterStatus() {
        disposable.add(
            characterDao.getCharacterById(character!!.id)
                .isEmpty
                .flatMapMaybe { isEmpty ->
                    Maybe.just(!isEmpty)
                }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe { isFavorite ->
                    //  updateFavoriteIcon(isFavorite)
                    _events.value = Event(CharacterDetailNavigation.UpdateFavoriteIcon(isFavorite))
                }
        )
    }

    fun onShowEpisodeList(episodeUrlList: List<String>) {
        disposable.add(
            Observable.fromIterable(episodeUrlList)
                .flatMap { episode: String ->
                    episodeRequest.baseUrl = episode
                    episodeRequest
                        .getService<EpisodeService>()
                        .getEpisode()
                        .toObservable()
                }
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe {
                    // episodeProgressBar.isVisible = true
                    _events.value = Event(CharacterDetailNavigation.ShowEpisodeLoading)
                }
                .subscribe(
                    { episodeList ->
                        //  episodeProgressBar.isVisible = false
                        _events.value = Event(CharacterDetailNavigation.HideEpisodeLoading)
                        //  episodeListAdapter.updateData(episodeList)
                        _events.value = Event(
                            CharacterDetailNavigation.ShowCharacterDetailEpisodeList(episodeList)
                        )
                    },
                    { error ->
                        //  episodeProgressBar.isVisible = false
                        _events.value = Event(CharacterDetailNavigation.HideEpisodeLoading)
                        _events.value =
                            Event(CharacterDetailNavigation.ShowCharacterDetailEpisodeError(error))
                    }
                )
        )
    }

    fun onUpdateFavoriteCharacterStatus() {
        val characterEntity: CharacterEntity = character!!.toCharacterEntity()
        disposable.add(
            characterDao.getCharacterById(characterEntity.id)
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
                .subscribeOn(Schedulers.io())
                .subscribe { isFavorite ->
                    //  updateFavoriteIcon(isFavorite)
                    _events.value = Event(CharacterDetailNavigation.UpdateFavoriteIcon(isFavorite))
                }
        )
    }

    fun onCharacterDetail() {
        if (character == null) {
            _events.value = Event(CharacterDetailNavigation.ShowCharacterDetailError)
        } else {
            _characterDetail.value = character
        }
    }

    sealed class CharacterDetailNavigation {
        object ShowCharacterDetailError : CharacterDetailNavigation()

        data class ShowCharacterDetailEpisodeError(val error: Throwable) :
            CharacterDetailNavigation()

        data class ShowCharacterDetailEpisodeList(val episodeList: List<EpisodeServer>) :
            CharacterDetailNavigation()

        data class UpdateFavoriteIcon(val isFavorite: Boolean) : CharacterDetailNavigation()

        object HideEpisodeLoading : CharacterDetailNavigation()
        object ShowEpisodeLoading : CharacterDetailNavigation()
    }
}
