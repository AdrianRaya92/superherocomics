package com.ayardreams.superherocomics.ui.comics

import android.Manifest
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.ayardreams.domain.Error
import com.ayardreams.domain.MarvelComics
import com.ayardreams.superherocomics.R
import com.ayardreams.superherocomics.ui.common.PermissionRequester
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun Fragment.buildComicsState(
    context: Context = requireContext(),
    scope: CoroutineScope = viewLifecycleOwner.lifecycleScope,
    writeExternalPermissionRequester: PermissionRequester = PermissionRequester(
        this,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    ),
    navController: NavController = findNavController()
) = ComicsState(context, scope, writeExternalPermissionRequester, navController)

class ComicsState(
    private val context: Context,
    private val scope: CoroutineScope,
    private val writeExternalPermissionRequester: PermissionRequester,
    private val navController: NavController
) {

    fun onComicClicked(comic: MarvelComics) {
        val action = ComicsFragmentDirections.actionMainToDetail(comic.id)
        navController.navigate(action)
    }

    fun requestWriteExternalPermission(afterRequest: (Boolean) -> Unit) {
        scope.launch {
            val granted = writeExternalPermissionRequester.request {
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
