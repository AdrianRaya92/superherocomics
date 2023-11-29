package com.ayardreams.superherocomics.ui.characters

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ayardreams.domain.MarvelComics
import com.ayardreams.superherocomics.R
import com.ayardreams.superherocomics.databinding.ViewComicsBinding
import com.ayardreams.superherocomics.ui.common.basicDiffUtil
import com.ayardreams.superherocomics.ui.common.inflate

class ComicsAdapter(private val listener: (MarvelComics) -> Unit) :
    ListAdapter<MarvelComics, ComicsAdapter.ViewHolder>(basicDiffUtil { old, new -> old.id == new.id }) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = parent.inflate(R.layout.view_comics, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val character = getItem(position)
        holder.bind(character)
        holder.itemView.setOnClickListener { listener(character) }
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ViewComicsBinding.bind(view)
        fun bind(marvelComics: MarvelComics) {
            binding.comics = marvelComics
        }
    }
}
