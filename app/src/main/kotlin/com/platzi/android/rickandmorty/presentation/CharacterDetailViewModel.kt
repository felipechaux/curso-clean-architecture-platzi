package com.platzi.android.rickandmorty.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.platzi.android.rickandmorty.api.CharacterServer
import com.platzi.android.rickandmorty.api.EpisodeServer
import com.platzi.android.rickandmorty.api.toCharacterEntity
import com.platzi.android.rickandmorty.database.CharacterEntity
import com.platzi.android.rickandmorty.usecases.GetEpisodeFromCharacterUseCase
import com.platzi.android.rickandmorty.usecases.GetFavoriteStatusUseCase
import com.platzi.android.rickandmorty.usecases.UpdateFavoriteStatusUseCase
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CharacterDetailViewModel(
    private val character: CharacterServer?,
    private val getEpisodeFromCharacterUseCase: GetEpisodeFromCharacterUseCase,
    private val getFavoriteStatusUseCase: GetFavoriteStatusUseCase,
    private val updateFavoriteStatusUseCase: UpdateFavoriteStatusUseCase
) : ViewModel() {

    private val disposable = CompositeDisposable()

    private val _events = MutableLiveData<Event<CharacterDetailNavigation>>()
    val events: LiveData<Event<CharacterDetailNavigation>> get() = _events

    private val _characterDetail = MutableLiveData<CharacterServer>()

    val characterDetail: MutableLiveData<CharacterServer>
        get() = _characterDetail

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> get() = _isFavorite

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    private fun onValidateFavoriteCharacterStatus(characterId: Int) {
        disposable.add(
            getFavoriteStatusUseCase.invoke(characterId)
                .subscribe { isFavorite ->
                    //  updateFavoriteIcon(isFavorite)
                    _isFavorite.value = isFavorite
                }
        )
    }

    private fun requestShowEpisodeList(episodeUrlList: List<String>) {
        disposable.add(
            getEpisodeFromCharacterUseCase.invoke(episodeUrlList).doOnSubscribe {
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

    fun updateFavoriteCharacterStatus() {
        val characterEntity: CharacterEntity = character!!.toCharacterEntity()
        disposable.add(
            updateFavoriteStatusUseCase.invoke(characterEntity)
                .subscribeOn(Schedulers.io())
                .subscribe { isFavorite ->
                    //  updateFavoriteIcon(isFavorite)
                    _isFavorite.value = isFavorite
                }
        )
    }

    fun onCharacterValidation() {
        if (character == null) {
            _events.value = Event(CharacterDetailNavigation.ShowCharacterDetailError)
        } else {
            _characterDetail.value = character
            onValidateFavoriteCharacterStatus(character.id)
            requestShowEpisodeList(character.episodeList)
        }
    }

    sealed class CharacterDetailNavigation {
        object ShowCharacterDetailError : CharacterDetailNavigation()

        data class ShowCharacterDetailEpisodeError(val error: Throwable) :
            CharacterDetailNavigation()

        data class ShowCharacterDetailEpisodeList(val episodeList: List<EpisodeServer>) :
            CharacterDetailNavigation()

        object HideEpisodeLoading : CharacterDetailNavigation()
        object ShowEpisodeLoading : CharacterDetailNavigation()
    }
}
