package com.ayardreams.superherocomics.ui.characters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ayardreams.domain.MarvelCharacter
import com.ayardreams.superherocomics.R
import com.ayardreams.superherocomics.databinding.ViewCharacterBinding
import com.ayardreams.superherocomics.ui.common.basicDiffUtil
import com.ayardreams.superherocomics.ui.common.inflate

class CharactersAdapter(private val listener: (MarvelCharacter) -> Unit) :
    ListAdapter<MarvelCharacter, CharactersAdapter.ViewHolder>(basicDiffUtil { old, new -> old.id == new.id }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = parent.inflate(R.layout.view_character, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val character = getItem(position)
        holder.bind(character)
        holder.itemView.setOnClickListener { listener(character) }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ViewCharacterBinding.bind(view)
        fun bind(marvelCharacter: MarvelCharacter) {
            binding.character = marvelCharacter
        }
    }
}
