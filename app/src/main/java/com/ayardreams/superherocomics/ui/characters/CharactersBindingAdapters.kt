package com.ayardreams.superherocomics.ui.characters

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ayardreams.domain.MarvelCharacter

@BindingAdapter("items")
fun RecyclerView.setItems(movies: List<MarvelCharacter>?) {
    if (movies != null) {
        (adapter as? CharactersAdapter)?.submitList(movies)
    }
}