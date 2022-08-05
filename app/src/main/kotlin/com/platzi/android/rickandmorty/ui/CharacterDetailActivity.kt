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
import com.platzi.android.rickandmorty.api.CharacterServer
import com.platzi.android.rickandmorty.api.EpisodeRequest
import com.platzi.android.rickandmorty.database.CharacterDao
import com.platzi.android.rickandmorty.database.CharacterDatabase
import com.platzi.android.rickandmorty.databinding.ActivityCharacterDetailBinding
import com.platzi.android.rickandmorty.presentation.CharacterDetailViewModel
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

    private val characterDao: CharacterDao by lazy {
        CharacterDatabase.getDatabase(application).characterDao()
    }

    private var character: CharacterServer? = null

    private val characterDetailViewModel: CharacterDetailViewModel by lazy {
        // getViewModel use for  CharacterDetailViewModel (,,)
        getViewModel {
            CharacterDetailViewModel(
                characterDao,
                intent.getParcelableExtra(Constants.EXTRA_CHARACTER),
                episodeRequest
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

        characterDetailViewModel.events.observe(
            this,
            Observer { events ->
                events?.getContentIfNotHandled()?.let { navigation ->
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
                        is CharacterDetailViewModel.CharacterDetailNavigation.UpdateFavoriteIcon -> navigation.run {
                            updateFavoriteIcon(isFavorite)
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
        )

        characterDetailViewModel.characterDetail.observe(
            this,
            Observer {
                binding.characterImage.bindCircularImageUrl(
                    url = it.image,
                    placeholder = R.drawable.ic_camera_alt_black,
                    errorPlaceholder = R.drawable.ic_broken_image_black
                )
                binding.characterDataName = it.name
                binding.characterDataStatus = it.status
                binding.characterDataSpecies = it.species
                binding.characterDataGender = it.gender
                binding.characterDataOriginName = it.origin.name
                binding.characterDataLocationName = it.location.name
            }
        )

        characterDetailViewModel.onCharacterDetail()

        characterDetailViewModel.onValidateFavoriteCharacterStatus()

        characterDetailViewModel.onShowEpisodeList(character!!.episodeList)

        characterFavorite.setOnClickListener { characterDetailViewModel.onUpdateFavoriteCharacterStatus() }
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

    //endregion
}
