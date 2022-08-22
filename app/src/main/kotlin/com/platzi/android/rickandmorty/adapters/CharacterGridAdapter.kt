package com.platzi.android.rickandmorty.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.platzi.android.rickandmorty.R
import com.platzi.android.rickandmorty.databinding.ItemGridCharacterBinding
import com.platzi.android.rickandmorty.domain.Character
import com.platzi.android.rickandmorty.utils.bindImageUrl
import com.platzi.android.rickandmorty.utils.bindingInflate
import kotlinx.android.synthetic.main.item_grid_character.view.*

class CharacterGridAdapter(
    private val listener: (Character) -> Unit
<<<<<<< HEAD
) : RecyclerView.Adapter<CharacterGridAdapter.CharacterGridViewHolder>() {
=======
): RecyclerView.Adapter<CharacterGridAdapter.CharacterGridViewHolder>() {
>>>>>>> feat/step_19/extra_use_cases_module

    private val characterList: MutableList<Character> = mutableListOf()

    fun addData(newData: List<Character>) {
        characterList.addAll(newData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        CharacterGridViewHolder(
            parent.bindingInflate(R.layout.item_grid_character, false),
            listener
        )

    override fun getItemCount() = characterList.size

    override fun getItemId(position: Int): Long = characterList[position].id.toLong()

    override fun onBindViewHolder(holder: CharacterGridViewHolder, position: Int) {
        holder.bind(characterList[position])
    }

    class CharacterGridViewHolder(
        private val dataBinding: ItemGridCharacterBinding,
        private val listener: (Character) -> Unit
<<<<<<< HEAD
    ) : RecyclerView.ViewHolder(dataBinding.root) {

        //region Public Methods
        fun bind(item: Character) {
=======
    ): RecyclerView.ViewHolder(dataBinding.root) {

        //region Public Methods
        fun bind(item: Character){
>>>>>>> feat/step_19/extra_use_cases_module
            dataBinding.character = item
            itemView.character_image.bindImageUrl(
                url = item.image,
                placeholder = R.drawable.ic_camera_alt_black,
                errorPlaceholder = R.drawable.ic_broken_image_black
            )
            itemView.setOnClickListener { listener(item) }
        }
    }
}
