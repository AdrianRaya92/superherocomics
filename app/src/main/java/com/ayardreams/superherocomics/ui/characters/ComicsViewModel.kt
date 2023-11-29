package com.ayardreams.superherocomics.ui.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayardreams.domain.Error
import com.ayardreams.domain.MarvelComics
import com.ayardreams.superherocomics.data.toError
import com.ayardreams.usecase.GetComicsUseCase
import com.ayardreams.usecase.RequestMarvelComicsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class ComicsViewModel @Inject constructor(
    getComicsUseCase: GetComicsUseCase,
    private val requestMarvelComicsUseCase: RequestMarvelComicsUseCase
) : ViewModel() {
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private var isLoading = false

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            getComicsUseCase()
                .catch { cause -> _state.update { it.copy(error = cause.toError()) } }
                .collect { comics -> _state.update { UiState(marvelComics = comics) } }
        }
    }

    fun loadMoreCharacters(offset: Int) {
        if (isLoading) {
            return
        }
        viewModelScope.launch {
            try {
                isLoading = true
                _state.value = _state.value.copy(loading = true)
                val error = requestMarvelComicsUseCase(
                    getCurrentDateFormatted(),
                    "${getDateSevenDaysAgoFormatted()},${getCurrentDateFormatted()}",
                    offset
                )
                _state.value = _state.value.copy(loading = false, error = error)
            } catch (cause: Throwable) {
                _state.update { it.copy(error = cause.toError()) }
            } finally {
                isLoading = false
            }
        }
    }

    fun onUiReady() {
        viewModelScope.launch {
            _state.value = _state.value.copy(loading = true)
            val error = requestMarvelComicsUseCase(
                getCurrentDateFormatted(),
                "${getDateSevenDaysAgoFormatted()},${getCurrentDateFormatted()}",
                0
            )
            _state.value = _state.value.copy(loading = false, error = error)
        }
    }

    private fun getCurrentDateFormatted(): String {
        val currentDate = LocalDate.now()
        return currentDate.format(dateFormatter)
    }

    private fun getDateSevenDaysAgoFormatted(): String {
        val sevenDaysAgo = LocalDate.now().minusDays(7)
        return sevenDaysAgo.format(dateFormatter)
    }

    data class UiState(
        var loading: Boolean = false,
        val marvelComics: List<MarvelComics>? = null,
        val error: Error? = null
    )
}
