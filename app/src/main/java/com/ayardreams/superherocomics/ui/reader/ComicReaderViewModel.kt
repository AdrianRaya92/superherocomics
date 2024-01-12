package com.ayardreams.superherocomics.ui.reader

import androidx.lifecycle.ViewModel
import com.ayardreams.domain.Error
import com.ayardreams.domain.ReaderComics
import com.ayardreams.usecase.GetComicsTotalUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class ComicReaderViewModel @Inject constructor() : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    fun onUiReady() {}

    data class UiState(
        var loading: Boolean = false,
        val readerComics: ReaderComics? = null,
        val error: Error? = null,
    )
}
