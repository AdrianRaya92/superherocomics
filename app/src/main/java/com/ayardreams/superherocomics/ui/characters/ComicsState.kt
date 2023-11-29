package com.ayardreams.superherocomics.ui.characters

import android.content.Context
import androidx.fragment.app.Fragment
import com.ayardreams.domain.Error
import com.ayardreams.superherocomics.R

fun Fragment.buildComicsState(
    context: Context = requireContext()
) = ComicsState(context)

class ComicsState(
    private val context: Context
) {

    fun errorToString(error: Error) = when (error) {
        Error.Connectivity -> context.getString(R.string.connectivity_error)
        is Error.Server -> context.getString(R.string.server_error) + error.code
        is Error.Unknown -> context.getString(R.string.unknown_error) + error.message
    }
}