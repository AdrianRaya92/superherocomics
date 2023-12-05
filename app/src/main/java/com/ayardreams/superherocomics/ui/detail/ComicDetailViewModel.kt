package com.ayardreams.superherocomics.ui.detail

import android.graphics.Bitmap
import android.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayardreams.domain.Error
import com.ayardreams.domain.MarvelComics
import com.ayardreams.superherocomics.data.toError
import com.ayardreams.superherocomics.di.ComicsId
import com.ayardreams.usecase.FindComicUseCase
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ComicDetailViewModel @Inject constructor(
    @ComicsId private val comicId: Int,
    findComicUseCase: FindComicUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(UiState())
    val state: StateFlow<UiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            findComicUseCase(comicId)
                .catch { cause -> _state.update { it.copy(error = cause.toError()) } }
                .collect { comic -> _state.update { UiState(comic = comic) } }
        }
    }

    fun generateQRCode(data: String): Bitmap {
        val writer = QRCodeWriter()
        val bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, 512, 512)
        val width = bitMatrix.width
        val height = bitMatrix.height
        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        for (x in 0 until width) {
            for (y in 0 until height) {
                bmp.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
            }
        }
        return bmp
    }

    data class UiState(val comic: MarvelComics? = null, val error: Error? = null)
}
