package com.ayardreams.superherocomics.ui.reader

import android.Manifest
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.ayardreams.domain.Error
import com.ayardreams.superherocomics.R
import com.ayardreams.superherocomics.ui.common.PermissionRequester
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun Fragment.buildComicsReaderState(
    context: Context = requireContext(),
    scope: CoroutineScope = viewLifecycleOwner.lifecycleScope,
    cameraPermissionRequester: PermissionRequester = PermissionRequester(
        this,
        Manifest.permission.CAMERA
    ),
) = ComicsReaderState(context, scope, cameraPermissionRequester)

class ComicsReaderState(
    private val context: Context,
    private val scope: CoroutineScope,
    private val cameraPermissionRequester: PermissionRequester
) {

    fun requestCameraPermission(afterRequest: (Boolean) -> Unit) {
        scope.launch {
            val granted = cameraPermissionRequester.request {
                afterRequest(false)
            }
            if (granted) afterRequest(true)
        }
    }

    fun errorToString(error: Error) = when (error) {
        Error.Connectivity -> context.getString(R.string.connectivity_error)
        is Error.Server -> context.getString(R.string.server_error) + error.code
        is Error.Unknown -> context.getString(R.string.unknown_error) + error.message
    }
}
