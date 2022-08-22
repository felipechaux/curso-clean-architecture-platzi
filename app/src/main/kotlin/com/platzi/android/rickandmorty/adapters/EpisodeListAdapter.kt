package com.platzi.android.rickandmorty.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.platzi.android.rickandmorty.R
import com.platzi.android.rickandmorty.databinding.ItemListEpisodeBinding
import com.platzi.android.rickandmorty.domain.Episode
import com.platzi.android.rickandmorty.utils.bindingInflate

class EpisodeListAdapter(
    private val listener: (Episode) -> Unit
<<<<<<< HEAD
) : RecyclerView.Adapter<EpisodeListAdapter.EpisodeListViewHolder>() {
=======
): RecyclerView.Adapter<EpisodeListAdapter.EpisodeListViewHolder>() {
>>>>>>> feat/step_19/extra_use_cases_module

    private val episodeList: MutableList<Episode> = mutableListOf()

    fun updateData(newData: List<Episode>) {
        episodeList.clear()
        episodeList.addAll(newData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        EpisodeListViewHolder(
            parent.bindingInflate(R.layout.item_list_episode, false),
            listener
        )

    override fun getItemCount() = episodeList.size

    override fun onBindViewHolder(holder: EpisodeListViewHolder, position: Int) {
        holder.bind(episodeList[position])
    }

    class EpisodeListViewHolder(
        private val dataBinding: ItemListEpisodeBinding,
        private val listener: (Episode) -> Unit
<<<<<<< HEAD
    ) : RecyclerView.ViewHolder(dataBinding.root) {

        //region Public Methods
        fun bind(item: Episode) {
=======
    ): RecyclerView.ViewHolder(dataBinding.root) {

        fun bind(item: Episode){
>>>>>>> feat/step_19/extra_use_cases_module
            dataBinding.episode = item
            itemView.setOnClickListener { listener(item) }
        }
    }
}
