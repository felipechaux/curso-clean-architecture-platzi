package com.platzi.android.rickandmorty.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
<<<<<<< HEAD
import com.platzi.android.rickandmorty.api.*
import com.platzi.android.rickandmorty.domain.Character
=======
import com.platzi.android.rickandmorty.domain.Character
import com.platzi.android.rickandmorty.presentation.CharacterListViewModel.CharacterListNavigation.*
import com.platzi.android.rickandmorty.presentation.utils.Event
>>>>>>> feat/step_19/extra_use_cases_module
import com.platzi.android.rickandmorty.usecases.GetAllCharactersUseCase
import io.reactivex.disposables.CompositeDisposable

class CharacterListViewModel(
    private val getAllCharactersUseCase: GetAllCharactersUseCase
<<<<<<< HEAD
) : ViewModel() {
=======
): ViewModel() {

    //region Fields

>>>>>>> feat/step_19/extra_use_cases_module
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
<<<<<<< HEAD
            getAllCharactersUseCase.invoke(currentPage).doOnSubscribe {
                _events.value = Event(CharacterListNagivation.ShowLoading)
            }
=======
            getAllCharactersUseCase
                .invoke(currentPage)
                .doOnSubscribe { showLoading() }
>>>>>>> feat/step_19/extra_use_cases_module
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

<<<<<<< HEAD
=======
    //endregion

    //region Private Methods

    private fun isInFooter(
        visibleItemCount: Int,
        firstVisibleItemPosition: Int,
        totalItemCount: Int
    ): Boolean {
        return visibleItemCount + firstVisibleItemPosition >= totalItemCount
                && firstVisibleItemPosition >= 0
                && totalItemCount >= PAGE_SIZE
    }

    private fun showLoading() {
        isLoading = true
        _events.value = Event(ShowLoading)
    }

    private fun hideLoading() {
        isLoading = false
        _events.value = Event(HideLoading)
    }

    //endregion

    //region Inner Classes & Interfaces

    sealed class CharacterListNavigation {
        data class ShowCharacterError(val error: Throwable) : CharacterListNavigation()
        data class ShowCharacterList(val characterList: List<Character>) : CharacterListNavigation()
        object HideLoading : CharacterListNavigation()
        object ShowLoading : CharacterListNavigation()
    }

    //endregion

    //region Companion Object

>>>>>>> feat/step_19/extra_use_cases_module
    companion object {
        private const val PAGE_SIZE = 20
    }

    // clase para indicar estados
    sealed class CharacterListNagivation {
        data class ShowCharacterError(val error: Throwable) : CharacterListNagivation()
        data class ShowCharacterList(val characterList: List<Character>) :
            CharacterListNagivation()

        object HideLoading : CharacterListNagivation()
        object ShowLoading : CharacterListNagivation()
    }
}
