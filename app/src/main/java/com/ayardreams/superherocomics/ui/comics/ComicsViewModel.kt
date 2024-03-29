package com.ayardreams.superherocomics.ui.comics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayardreams.domain.Error
import com.ayardreams.domain.MarvelApi
import com.ayardreams.domain.MarvelComics
import com.ayardreams.superherocomics.data.toError
import com.ayardreams.usecase.GetComicsTotalUseCase
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
    private val requestMarvelComicsUseCase: RequestMarvelComicsUseCase,
    private val getComicsTotalUseCase: GetComicsTotalUseCase,
) : ViewModel() {
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            getComicsUseCase()
                .catch { cause -> _state.update { it.copy(error = cause.toError()) } }
                .collect { comics -> _state.update { it.copy(marvelComics = comics) } }
        }
    }

    fun onUiReady() {
        viewModelScope.launch {
            var offset = 0
            do {
                _state.value = _state.value.copy(
                    loading = true,
                    dateComics = getDateToolbar()
                )
                val error = requestMarvelComicsUseCase(
                    getCurrentDateFormatted(),
                    "${getDateSevenDaysAgoFormatted()},${getCurrentDateFormatted()}",
                    offset
                )
                if (error == null) {
                    _state.value = _state.value.copy(
                        loading = false,
                        totalComics = getComicsTotalUseCase().toString()
                    )
                    offset += MarvelApi.limit
                } else {
                    _state.value = _state.value.copy(loading = false, error = error)
                    break
                }
            } while (getComicsTotalUseCase() % MarvelApi.limit == 0)
        }
    }

    private fun getCurrentDateFormatted(): String {
        val currentDate = LocalDate.now()
        return currentDate.format(dateFormatter)
    }

    private fun getDateSevenDaysAgoFormatted(): String {
        val sevenDaysAgo = LocalDate.now().minusDays(MarvelApi.newsComicsDays)
        return sevenDaysAgo.format(dateFormatter)
    }

    private fun getDateToolbar(): String {
        val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val currentDate = LocalDate.now().format(dateFormatter)
        val sevenDaysAgo = LocalDate.now().minusDays(MarvelApi.newsComicsDays).format(dateFormatter)
        return "$sevenDaysAgo - $currentDate"
    }

    data class UiState(
        var loading: Boolean = false,
        val marvelComics: List<MarvelComics>? = null,
        val error: Error? = null,
        val totalComics: String = "0",
        val dateComics: String = ""
    )
}
