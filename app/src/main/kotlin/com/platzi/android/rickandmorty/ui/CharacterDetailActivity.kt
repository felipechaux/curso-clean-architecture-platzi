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
import com.platzi.android.rickandmorty.data.* // ktlint-disable no-wildcard-imports
import com.platzi.android.rickandmorty.database.CharacterDatabase
import com.platzi.android.rickandmorty.database.CharacterRoomDataSource
import com.platzi.android.rickandmorty.databinding.ActivityCharacterDetailBinding
import com.platzi.android.rickandmorty.domain.Character
import com.platzi.android.rickandmorty.parcelable.CharacterParcelable
import com.platzi.android.rickandmorty.parcelable.toCharacterDomain
import com.platzi.android.rickandmorty.presentation.CharacterDetailViewModel
import com.platzi.android.rickandmorty.presentation.Event
import com.platzi.android.rickandmorty.usecases.GetEpisodeFromCharacterUseCase
import com.platzi.android.rickandmorty.usecases.GetFavoriteStatusUseCase
import com.platzi.android.rickandmorty.usecases.UpdateFavoriteStatusUseCase
import com.platzi.android.rickandmorty.utils.Constants
import com.platzi.android.rickandmorty.utils.bindCircularImageUrl
import com.platzi.android.rickandmorty.utils.getViewModel
import com.platzi.android.rickandmorty.utils.showLongToast
import kotlinx.android.synthetic.main.activity_character_detail.*

class CharacterDetailActivity : AppCompatActivity() {

    //region Fields

    //  private val disposable = CompositeDisposable()

    private lateinit var episodeListAdapter: EpisodeListAdapter
    private lateinit var binding: ActivityCharacterDetailBinding

    private val episodeRequest: EpisodeRequest by lazy {
        EpisodeRequest(BASE_API_URL)
    }

    private val characterRequest: CharacterRequest by lazy {
        CharacterRequest(BASE_API_URL)
    }

    private val remoteCharacterDataSource: RemoteCharacterDataSource by lazy {
        CharacterRetrofitDataSource(characterRequest)
    }

    private val remoteEpisodeDataSource: RemoteEpisodeDataSource by lazy {
        EpisodeRetrofitDataSource(episodeRequest)
    }

    private val localCharacterDataSource: LocalCharacterDataSource by lazy {
        CharacterRoomDataSource(CharacterDatabase.getDatabase(applicationContext))
    }

    private val characterRepository: CharacterRepository by lazy {
        CharacterRepository(remoteCharacterDataSource, localCharacterDataSource)
    }

    private val episodeRepository: EpisodeRepository by lazy {
        EpisodeRepository(remoteEpisodeDataSource)
    }

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
            }
        }
    }
    //endregion
}
