package com.ayardreams.superherocomics.ui.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayardreams.domain.Error
import com.ayardreams.domain.MarvelApi
import com.ayardreams.domain.MarvelCharacter
import com.ayardreams.superherocomics.data.toError
import com.ayardreams.usecase.GetCharactersUseCase
import com.ayardreams.usecase.RequestMarvelCharactersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(
    getCharactersUseCase: GetCharactersUseCase,
    private val requestMarvelCharactersUseCase: RequestMarvelCharactersUseCase
) : ViewModel() {
    private var offset = 0

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            getCharactersUseCase()
                .catch { cause -> _state.update { it.copy(error = cause.toError()) } }
                .collect { characters -> _state.update { UiState(marvelCharacters = characters) } }
        }
    }

    fun onUiReady() {
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true)
            val error = requestMarvelCharactersUseCase(offset)
            _state.value = _state.value.copy(loading = false, error = error)
        }
    }

    data class UiState(
        var loading: Boolean = true,
        val marvelCharacters: List<MarvelCharacter>? = null,
        val error: Error? = null
    )
}
