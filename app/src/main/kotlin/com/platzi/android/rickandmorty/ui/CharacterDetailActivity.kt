package com.platzi.android.rickandmorty.ui

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.platzi.android.rickandmorty.R
import com.platzi.android.rickandmorty.adapters.EpisodeListAdapter
import com.platzi.android.rickandmorty.api.APIConstants.BASE_API_URL
import com.platzi.android.rickandmorty.api.CharacterRequest
import com.platzi.android.rickandmorty.api.CharacterRetrofitDataSource
import com.platzi.android.rickandmorty.api.EpisodeRequest
import com.platzi.android.rickandmorty.api.EpisodeRetrofitDataSource
<<<<<<< HEAD
import com.platzi.android.rickandmorty.data.* // ktlint-disable no-wildcard-imports
=======
import com.platzi.android.rickandmorty.data.*
>>>>>>> feat/step_19/extra_use_cases_module
import com.platzi.android.rickandmorty.database.CharacterDatabase
import com.platzi.android.rickandmorty.database.CharacterRoomDataSource
import com.platzi.android.rickandmorty.databinding.ActivityCharacterDetailBinding
import com.platzi.android.rickandmorty.domain.Character
<<<<<<< HEAD
import com.platzi.android.rickandmorty.parcelable.CharacterParcelable
import com.platzi.android.rickandmorty.parcelable.toCharacterDomain
import com.platzi.android.rickandmorty.presentation.CharacterDetailViewModel
import com.platzi.android.rickandmorty.presentation.Event
import com.platzi.android.rickandmorty.usecases.GetEpisodeFromCharacterUseCase
import com.platzi.android.rickandmorty.usecases.GetFavoriteStatusUseCase
import com.platzi.android.rickandmorty.usecases.UpdateFavoriteStatusUseCase
=======
import com.platzi.android.rickandmorty.parcelables.CharacterParcelable
import com.platzi.android.rickandmorty.parcelables.toCharacterDomain
import com.platzi.android.rickandmorty.presentation.CharacterDetailViewModel
import com.platzi.android.rickandmorty.presentation.CharacterDetailViewModel.CharacterDetailNavigation
import com.platzi.android.rickandmorty.presentation.CharacterDetailViewModel.CharacterDetailNavigation.*
import com.platzi.android.rickandmorty.presentation.utils.Event
import com.platzi.android.rickandmorty.usecases.GetEpisodeFromCharacterUseCase
import com.platzi.android.rickandmorty.usecases.GetFavoriteCharacterStatusUseCase
import com.platzi.android.rickandmorty.usecases.UpdateFavoriteCharacterStatusUseCase
>>>>>>> feat/step_19/extra_use_cases_module
import com.platzi.android.rickandmorty.utils.Constants
import com.platzi.android.rickandmorty.utils.bindCircularImageUrl
import com.platzi.android.rickandmorty.utils.getViewModel
import com.platzi.android.rickandmorty.utils.showLongToast
import kotlinx.android.synthetic.main.activity_character_detail.*

class CharacterDetailActivity : AppCompatActivity() {

    //region Fields

<<<<<<< HEAD
    //  private val disposable = CompositeDisposable()

=======
>>>>>>> feat/step_19/extra_use_cases_module
    private lateinit var episodeListAdapter: EpisodeListAdapter
    private lateinit var binding: ActivityCharacterDetailBinding

    private val episodeRequest: EpisodeRequest by lazy {
        EpisodeRequest(BASE_API_URL)
    }

    private val characterRequest: CharacterRequest by lazy {
        CharacterRequest(BASE_API_URL)
    }

<<<<<<< HEAD
    private val remoteCharacterDataSource: RemoteCharacterDataSource by lazy {
        CharacterRetrofitDataSource(characterRequest)
    }

    private val remoteEpisodeDataSource: RemoteEpisodeDataSource by lazy {
        EpisodeRetrofitDataSource(episodeRequest)
    }

=======
>>>>>>> feat/step_19/extra_use_cases_module
    private val localCharacterDataSource: LocalCharacterDataSource by lazy {
        CharacterRoomDataSource(CharacterDatabase.getDatabase(applicationContext))
    }

<<<<<<< HEAD
=======
    private val remoteCharacterDataSource: RemoteCharacterDataSource by lazy {
        CharacterRetrofitDataSource(characterRequest)
    }

>>>>>>> feat/step_19/extra_use_cases_module
    private val characterRepository: CharacterRepository by lazy {
        CharacterRepository(remoteCharacterDataSource, localCharacterDataSource)
    }

<<<<<<< HEAD
=======
    private val remoteEpisodeDataSource: RemoteEpisodeDataSource by lazy {
        EpisodeRetrofitDataSource(episodeRequest)
    }

>>>>>>> feat/step_19/extra_use_cases_module
    private val episodeRepository: EpisodeRepository by lazy {
        EpisodeRepository(remoteEpisodeDataSource)
    }

<<<<<<< HEAD
    private val getFavoriteStatusUseCase: GetFavoriteStatusUseCase by lazy {
        GetFavoriteStatusUseCase(characterRepository)
    }

    private val updateFavoriteStatusUseCase: UpdateFavoriteStatusUseCase by lazy {
        UpdateFavoriteStatusUseCase(characterRepository)
    }

    private val getEpisodeFromCharacterUseCase: GetEpisodeFromCharacterUseCase by lazy {
        GetEpisodeFromCharacterUseCase(episodeRepository)
    }

    private val characterDetailViewModel: CharacterDetailViewModel by lazy {
        // getViewModel use for  CharacterDetailViewModel (,,)
        getViewModel {
            CharacterDetailViewModel(
                intent.getParcelableExtra<CharacterParcelable>(Constants.EXTRA_CHARACTER)
                    ?.toCharacterDomain(),
                getEpisodeFromCharacterUseCase,
                getFavoriteStatusUseCase,
                updateFavoriteStatusUseCase
=======
    private val getEpisodeFromCharacterUseCase: GetEpisodeFromCharacterUseCase by lazy {
        GetEpisodeFromCharacterUseCase(episodeRepository)
    }

    private val getFavoriteCharacterStatusUseCase: GetFavoriteCharacterStatusUseCase by lazy {
        GetFavoriteCharacterStatusUseCase(characterRepository)
    }

    private val updateFavoriteCharacterStatusUseCase: UpdateFavoriteCharacterStatusUseCase by lazy {
        UpdateFavoriteCharacterStatusUseCase(characterRepository)
    }

    private val characterDetailViewModel: CharacterDetailViewModel by lazy {
        getViewModel {
            CharacterDetailViewModel(
                intent.getParcelableExtra<CharacterParcelable>(Constants.EXTRA_CHARACTER)?.toCharacterDomain(),
                getEpisodeFromCharacterUseCase,
                getFavoriteCharacterStatusUseCase,
                updateFavoriteCharacterStatusUseCase
>>>>>>> feat/step_19/extra_use_cases_module
            )
        }
    }

    //endregion

    //region Override Methods

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_character_detail)
        binding.lifecycleOwner = this@CharacterDetailActivity

        episodeListAdapter = EpisodeListAdapter { episode ->
            this@CharacterDetailActivity.showLongToast("Episode -> $episode")
        }
        rvEpisodeList.adapter = episodeListAdapter

<<<<<<< HEAD
        characterFavorite.setOnClickListener { characterDetailViewModel.updateFavoriteCharacterStatus() }

        characterDetailViewModel.characterDetail.observe(
            this,
            Observer(this::loadCharacterDetail)
        )
        characterDetailViewModel.isFavorite.observe(this, Observer(this::updateFavoriteIcon))
        characterDetailViewModel.events.observe(
            this,
            Observer(this::validateEvents)
        )
=======
        characterFavorite.setOnClickListener { characterDetailViewModel.onUpdateFavoriteCharacterStatus() }

        characterDetailViewModel.characterValues.observe(this, Observer(this::loadCharacter))
        characterDetailViewModel.isFavorite.observe(this, Observer(this::updateFavoriteIcon))
        characterDetailViewModel.events.observe(this, Observer(this::validateEvents))

>>>>>>> feat/step_19/extra_use_cases_module
        characterDetailViewModel.onCharacterValidation()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    //endregion

    //region Private Methods

<<<<<<< HEAD
    private fun updateFavoriteIcon(isFavorite: Boolean?) {
        characterFavorite.setImageResource(
            if (isFavorite != null && isFavorite) {
                R.drawable.ic_favorite
            } else {
                R.drawable.ic_favorite_border
            }
        )
    }

    private fun loadCharacterDetail(characterDetail: Character) {
        binding.characterImage.bindCircularImageUrl(
            url = characterDetail.image,
            placeholder = R.drawable.ic_camera_alt_black,
            errorPlaceholder = R.drawable.ic_broken_image_black
        )
        binding.characterDataName = characterDetail.name
        binding.characterDataStatus = characterDetail.status
        binding.characterDataSpecies = characterDetail.species
        binding.characterDataGender = characterDetail.gender
        binding.characterDataOriginName = characterDetail.origin.name
        binding.characterDataLocationName = characterDetail.location.name
    }

    private fun validateEvents(event: Event<CharacterDetailViewModel.CharacterDetailNavigation>) {
        event.getContentIfNotHandled()?.let { navigation ->
            when (navigation) {
                CharacterDetailViewModel.CharacterDetailNavigation.ShowCharacterDetailError -> {
                    this@CharacterDetailActivity.showLongToast(R.string.error_no_character_data.toString())
                    finish()
                }
                is CharacterDetailViewModel.CharacterDetailNavigation.ShowCharacterDetailEpisodeList -> navigation.run {
                    episodeListAdapter.updateData(episodeList)
                }
                is CharacterDetailViewModel.CharacterDetailNavigation.ShowCharacterDetailEpisodeError -> {
                    this.showLongToast("Error")
                }
                CharacterDetailViewModel.CharacterDetailNavigation.ShowEpisodeLoading -> {
                    episodeProgressBar.isVisible = true
                }
                CharacterDetailViewModel.CharacterDetailNavigation.HideEpisodeLoading -> {
                    episodeProgressBar.isVisible = false
                }
=======
    private fun loadCharacter(character: Character){
        binding.characterImage.bindCircularImageUrl(
            url = character.image,
            placeholder = R.drawable.ic_camera_alt_black,
            errorPlaceholder = R.drawable.ic_broken_image_black
        )
        binding.characterDataName = character.name
        binding.characterDataStatus = character.status
        binding.characterDataSpecies = character.species
        binding.characterDataGender = character.gender
        binding.characterDataOriginName = character.origin.name
        binding.characterDataLocationName = character.location.name
    }

    private fun updateFavoriteIcon(isFavorite: Boolean?){
        characterFavorite.setImageResource(
            if (isFavorite != null && isFavorite) {
                R.drawable.ic_favorite
            } else {
                R.drawable.ic_favorite_border
>>>>>>> feat/step_19/extra_use_cases_module
            }
        }
    }
<<<<<<< HEAD
=======

    private fun validateEvents(event: Event<CharacterDetailNavigation>?) {
        event?.getContentIfNotHandled()?.let { navigation ->
            when (navigation) {
                is ShowEpisodeError -> navigation.run {
                    this@CharacterDetailActivity.showLongToast("Error -> ${error.message}")
                }
                is ShowEpisodeList -> navigation.run {
                    episodeListAdapter.updateData(episodeList)
                }
                CloseActivity -> {
                    this@CharacterDetailActivity.showLongToast(R.string.error_no_character_data)
                    finish()
                }
                HideEpisodeListLoading -> {
                    episodeProgressBar.isVisible = false
                }
                ShowEpisodeListLoading -> {
                    episodeProgressBar.isVisible = true
                }
            }
        }
    }

>>>>>>> feat/step_19/extra_use_cases_module
    //endregion
}
