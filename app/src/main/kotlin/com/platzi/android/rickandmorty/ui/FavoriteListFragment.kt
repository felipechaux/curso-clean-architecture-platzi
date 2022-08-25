package com.platzi.android.rickandmorty.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.platzi.android.rickandmorty.R
import com.platzi.android.rickandmorty.adapters.FavoriteListAdapter
import com.platzi.android.rickandmorty.databinding.FragmentFavoriteListBinding
import com.platzi.android.rickandmorty.di.FavoriteListComponent
import com.platzi.android.rickandmorty.di.FavoriteListModule
import com.platzi.android.rickandmorty.domain.Character
import com.platzi.android.rickandmorty.presentation.FavoriteListViewModel
import com.platzi.android.rickandmorty.utils.app
import com.platzi.android.rickandmorty.utils.getViewModel
import com.platzi.android.rickandmorty.utils.setItemDecorationSpacing
import kotlinx.android.synthetic.main.fragment_favorite_list.*

class FavoriteListFragment : Fragment() {

    //region Fields

    private lateinit var favoriteListAdapter: FavoriteListAdapter
    private lateinit var listener: OnFavoriteListFragmentListener
    private lateinit var favoriteListComponent: FavoriteListComponent

    private val favoriteListViewModel: FavoriteListViewModel by lazy {
        getViewModel { favoriteListComponent.favoriteListViewModel }
    }
    //endregion

    //region Override Methods & Callbacks

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as OnFavoriteListFragmentListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement OnFavoriteListFragmentListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favoriteListComponent = context!!.app.component.inject(FavoriteListModule())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return DataBindingUtil.inflate<FragmentFavoriteListBinding>(
            inflater,
            R.layout.fragment_favorite_list,
            container,
            false
        ).apply {
            lifecycleOwner = this@FavoriteListFragment
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoriteListAdapter = FavoriteListAdapter { character ->
            listener.openCharacterDetail(character)
        }
        favoriteListAdapter.setHasStableIds(true)

        rvFavoriteList.run {
            setItemDecorationSpacing(resources.getDimension(R.dimen.list_item_padding))
            adapter = favoriteListAdapter
        }

        // lo que se obtenga del live data volver a enviar a viewmodel para validar lista
        favoriteListViewModel.favoriteCharacterList.observe(
            this,
            Observer(favoriteListViewModel::onFavoriteCharacterList)
        )
        favoriteListViewModel.events.observe(
            this,
            Observer { events ->
                events?.getContentIfNotHandled()?.let { navigation ->
                    when (navigation) {
                        is FavoriteListViewModel.FavoriteListNavigation.ShowCharacterList -> navigation.run {
                            tvEmptyListMessage.isVisible = false
                            favoriteListAdapter.updateData(characterList)
                        }
                        FavoriteListViewModel.FavoriteListNavigation.ShowEmptyListMessage -> {
                            tvEmptyListMessage.isVisible = true
                            favoriteListAdapter.updateData(emptyList())
                        }
                    }
                }
            }
        )
    }

    //endregion

    //region Private Methods

    //endregion

    //region Inner Classes & Interfaces

    interface OnFavoriteListFragmentListener {
        fun openCharacterDetail(character: Character)
    }

    //endregion

    //region Companion object

    companion object {

        fun newInstance(args: Bundle? = Bundle()) = FavoriteListFragment().apply {
            arguments = args
        }
    }

    //endregion
}
