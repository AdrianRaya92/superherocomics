package com.ayardreams.superherocomics.ui.comics

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ayardreams.domain.MarvelComics

@BindingAdapter("items")
fun RecyclerView.setItems(comics: List<MarvelComics>?) {
    if (comics != null) {
        (adapter as? ComicsAdapter)?.submitList(comics)
    }
}