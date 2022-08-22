package com.platzi.android.rickandmorty.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
<<<<<<< HEAD
import com.platzi.android.rickandmorty.database.CharacterEntity
import com.platzi.android.rickandmorty.domain.Character
import com.platzi.android.rickandmorty.usecases.GetAllFavoriteCharacterUseCase
import io.reactivex.disposables.CompositeDisposable

class FavoriteListViewModel(
    private val getAllFavoriteCharacterUseCase: GetAllFavoriteCharacterUseCase
) : ViewModel() {
=======
import com.platzi.android.rickandmorty.domain.Character
import com.platzi.android.rickandmorty.presentation.FavoriteListViewModel.FavoriteListNavigation.ShowCharacterList
import com.platzi.android.rickandmorty.presentation.FavoriteListViewModel.FavoriteListNavigation.ShowEmptyListMessage
import com.platzi.android.rickandmorty.presentation.utils.Event
import com.platzi.android.rickandmorty.usecases.GetAllFavoriteCharactersUseCase
import io.reactivex.disposables.CompositeDisposable

class FavoriteListViewModel (
    private val getAllFavoriteCharactersUseCase: GetAllFavoriteCharactersUseCase
) : ViewModel(){

    //region Fields
>>>>>>> feat/step_19/extra_use_cases_module

    private val disposable = CompositeDisposable()

    private val _events = MutableLiveData<Event<FavoriteListNavigation>>()
    val events: LiveData<Event<FavoriteListNavigation>> get() = _events

<<<<<<< HEAD
    // informacion cuando haya cambio en bd
    val favoriteCharacterList: LiveData<List<Character>>
        get() = LiveDataReactiveStreams.fromPublisher(getAllFavoriteCharacterUseCase.invoke())

   /* disposable.add(
    characterDao.getAllFavoriteCharacters()
    .observeOn(AndroidSchedulers.mainThread())
    .subscribeOn(Schedulers.io())
    .subscribe({ characterList ->
        if(characterList.isEmpty()) {
            tvEmptyListMessage.isVisible = true
            favoriteListAdapter.updateData(emptyList())
        } else {
            tvEmptyListMessage.isVisible = false
            favoriteListAdapter.updateData(characterList)
        }
    },{
        tvEmptyListMessage.isVisible = true
        favoriteListAdapter.updateData(emptyList())
    })
    )*/
=======
    val favoriteCharacterList: LiveData<List<Character>>
        get() = LiveDataReactiveStreams.fromPublisher(getAllFavoriteCharactersUseCase.invoke())

    //endregion

    //region Override Methods & Callbacks
>>>>>>> feat/step_19/extra_use_cases_module

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

<<<<<<< HEAD
    fun onFavoriteCharacterList(list: List<Character>) {
        if (list.isEmpty()) {
            _events.value = Event(FavoriteListNavigation.ShowEmptyListMessage)
            return
        }
        _events.value = Event(FavoriteListNavigation.ShowCharacterList(list))
    }

    sealed class FavoriteListNavigation {
        data class ShowCharacterList(val characterList: List<Character>) :
            FavoriteListNavigation()

        // cuando no hay nada que mostrar
        object ShowEmptyListMessage : FavoriteListNavigation()
    }
=======
    //endregion

    //region Public Methods

    fun onFavoriteCharacterList(favoriteCharacterList: List<Character>) {
        if (favoriteCharacterList.isEmpty()) {
            _events.value = Event(ShowCharacterList(emptyList()))
            _events.value = Event(ShowEmptyListMessage)
            return
        }

        _events.value = Event(ShowCharacterList(favoriteCharacterList))
    }

    //endregion

    //region Inner Classes & Interfaces

    sealed class FavoriteListNavigation {
        data class ShowCharacterList(val characterList: List<Character>) : FavoriteListNavigation()
        object ShowEmptyListMessage : FavoriteListNavigation()
    }

    //endregion

>>>>>>> feat/step_19/extra_use_cases_module
}
