package com.ayardreams.superherocomics.ui.detail

import android.content.ContentResolver
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Environment
import android.provider.MediaStore
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ComicDetailViewModel @Inject constructor(
    @ComicsId private val comicId: Int,
    findComicUseCase: FindComicUseCase
) : ViewModel() {

    lateinit var imageBmp: Bitmap

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
        imageBmp = bmp
        return bmp
    }

    fun saveImage(titleComic: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val fileName = "QR_${titleComic.replace(" ", "_").take(30)}_${System.currentTimeMillis()}.png"
                val storage = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                val file = File(storage, fileName)
                FileOutputStream(file).use { out ->
                    imageBmp.compress(Bitmap.CompressFormat.PNG, 100, out)
                }
                _state.update { it.copy(qrSave = true, messageStorage = "$storage") }
            } catch (e: IOException) {
                _state.update { it.copy(error = e.toError()) }
            }
        }
    }

    fun saveImage(contentResolver: ContentResolver, titleComic: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val fileName = "QR_${titleComic.replace(" ", "_").take(30)}_${System.currentTimeMillis()}.png"

                val values = ContentValues().apply {
                    put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
                    put(MediaStore.Images.Media.MIME_TYPE, "image/png")
                    put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
                }

                val uri =
                    contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

                uri?.let { it ->
                    contentResolver.openOutputStream(it).use { outputStream ->
                        imageBmp.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                    }
                    _state.update { it.copy(qrSave = true, messageStorage = "Pictures/$fileName") }
                } ?: throw IOException("Unable to create media store entry.")
            } catch (e: IOException) {
                _state.update { it.copy(error = e.toError()) }
            }
        }
    }

    data class UiState(
        val comic: MarvelComics? = null,
        val error: Error? = null,
        val qrSave: Boolean = false,
        val messageStorage: String = "",
    )
}
