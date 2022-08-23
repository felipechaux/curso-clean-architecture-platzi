package com.platzi.android.rickandmorty.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.LiveDataReactiveStreams
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.platzi.android.rickandmorty.domain.Character
import com.platzi.android.rickandmorty.usecases.GetAllFavoriteCharacterUseCase
import io.reactivex.disposables.CompositeDisposable

class FavoriteListViewModel(
    private val getAllFavoriteCharacterUseCase: GetAllFavoriteCharacterUseCase
) : ViewModel() {

    private val disposable = CompositeDisposable()

    private val _events = MutableLiveData<Event<FavoriteListNavigation>>()
    val events: LiveData<Event<FavoriteListNavigation>> get() = _events

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

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

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
}
