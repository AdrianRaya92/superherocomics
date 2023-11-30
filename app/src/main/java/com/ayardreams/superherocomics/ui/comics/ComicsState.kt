package com.ayardreams.superherocomics.ui.comics

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.ayardreams.domain.Error
import com.ayardreams.domain.MarvelComics
import com.ayardreams.superherocomics.R

fun Fragment.buildComicsState(
    context: Context = requireContext(),
    navController: NavController = findNavController()
) = ComicsState(context, navController)

class ComicsState(
    private val context: Context,
    private val navController: NavController
) {

    fun onComicClicked(comic: MarvelComics) {
        val action = ComicsFragmentDirections.actionMainToDetail(comic.id)
        navController.navigate(action)
    }

    fun errorToString(error: Error) = when (error) {
        Error.Connectivity -> context.getString(R.string.connectivity_error)
        is Error.Server -> context.getString(R.string.server_error) + error.code
        is Error.Unknown -> context.getString(R.string.unknown_error) + error.message
    }
}