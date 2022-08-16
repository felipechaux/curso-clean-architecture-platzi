package com.platzi.android.rickandmorty.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.platzi.android.rickandmorty.domain.Character
import com.platzi.android.rickandmorty.domain.Episode
import com.platzi.android.rickandmorty.usecases.GetEpisodeFromCharacterUseCase
import com.platzi.android.rickandmorty.usecases.GetFavoriteStatusUseCase
import com.platzi.android.rickandmorty.usecases.UpdateFavoriteStatusUseCase
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CharacterDetailViewModel(
    private val character: Character?,
    private val getEpisodeFromCharacterUseCase: GetEpisodeFromCharacterUseCase,
    private val getFavoriteStatusUseCase: GetFavoriteStatusUseCase,
    private val updateFavoriteStatusUseCase: UpdateFavoriteStatusUseCase
) : ViewModel() {

    private val disposable = CompositeDisposable()

    private val _events = MutableLiveData<Event<CharacterDetailNavigation>>()
    val events: LiveData<Event<CharacterDetailNavigation>> get() = _events

    private val _characterDetail = MutableLiveData<Character>()

    val characterDetail: MutableLiveData<Character>
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
        disposable.add(
            updateFavoriteStatusUseCase.invoke(character!!)
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

        data class ShowCharacterDetailEpisodeList(val episodeList: List<Episode>) :
            CharacterDetailNavigation()

        object HideEpisodeLoading : CharacterDetailNavigation()
        object ShowEpisodeLoading : CharacterDetailNavigation()
    }
}
