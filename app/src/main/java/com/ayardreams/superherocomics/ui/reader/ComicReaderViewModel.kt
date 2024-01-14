package com.ayardreams.superherocomics.ui.reader

import androidx.lifecycle.ViewModel
import com.ayardreams.domain.ReaderComics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ComicReaderViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    fun onUiReady(comic: ReaderComics?) {
        _state.update { it.copy(readerComics = comic) }
    }

    data class UiState(
        val readerComics: ReaderComics? = null
    )
}
