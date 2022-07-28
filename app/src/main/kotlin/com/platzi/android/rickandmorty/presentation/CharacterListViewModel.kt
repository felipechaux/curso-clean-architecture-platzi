package com.platzi.android.rickandmorty.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.platzi.android.rickandmorty.api.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CharacterListViewModel(private val characterRequest: CharacterRequest) : ViewModel() {
    private val disposable = CompositeDisposable()

    private val _events = MutableLiveData<Event<CharacterListNagivation>>()
    val events: LiveData<Event<CharacterListNagivation>> get() = _events

    private var currentPage = 1
    private var isLastPage = false
    private var isLoading = false

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    fun onLoadMoreItems(visibleItemCount: Int, firstVisibleItemPosition: Int, totalItemCount: Int) {
        if (isLoading || isLastPage || !isInFooter(
                visibleItemCount,
                firstVisibleItemPosition,
                totalItemCount
            )
        ) {
            return
        }

        currentPage += 1
        onGetAllCharacters()
    }

    private fun isInFooter(
        visibleItemCount: Int,
        firstVisibleItemPosition: Int,
        totalItemCount: Int
    ): Boolean {
        return visibleItemCount + firstVisibleItemPosition >= totalItemCount &&
            firstVisibleItemPosition >= 0 &&
            totalItemCount >= PAGE_SIZE
    }

    fun onRetryGetAllCharacter(itemCount: Int) {
        if (itemCount > 0) {
            _events.value = Event(CharacterListNagivation.HideLoading)
            return
        }

        onGetAllCharacters()
    }

    fun onGetAllCharacters() {
        disposable.add(
            characterRequest
                .getService<CharacterService>()
                .getAllCharacters(currentPage)
                .map(CharacterResponseServer::toCharacterServerList)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .doOnSubscribe {
                    _events.value = Event(CharacterListNagivation.ShowLoading)
                }
                .subscribe({ characterList ->
                    if (characterList.size < PAGE_SIZE) {
                        isLastPage = true
                    }
                    _events.value = Event(CharacterListNagivation.HideLoading)
                    _events.value = Event(CharacterListNagivation.ShowCharacterList(characterList))
                }, { error ->
                    isLastPage = true
                    _events.value = Event(CharacterListNagivation.HideLoading)
                    _events.value = Event(CharacterListNagivation.ShowCharacterError(error))
                })
        )
    }

    companion object {
        private const val PAGE_SIZE = 20
    }

    // clase para indicar estados
    sealed class CharacterListNagivation {
        data class ShowCharacterError(val error: Throwable) : CharacterListNagivation()
        data class ShowCharacterList(val characterList: List<CharacterServer>) :
            CharacterListNagivation()

        object HideLoading : CharacterListNagivation()
        object ShowLoading : CharacterListNagivation()
    }
}
