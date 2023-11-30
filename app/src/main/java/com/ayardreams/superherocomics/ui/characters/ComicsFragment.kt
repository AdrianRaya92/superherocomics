package com.ayardreams.superherocomics.ui.characters

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ayardreams.superherocomics.R
import com.ayardreams.superherocomics.databinding.FragmentComicsBinding
import com.ayardreams.superherocomics.ui.common.launchAndCollect
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ComicsFragment : Fragment(R.layout.fragment_comics) {
    private val viewModel: ComicsViewModel by viewModels()
    private lateinit var comicsState: ComicsState
    private val adapter = ComicsAdapter {
        // characterState.onMovieClicked(it)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        comicsState = buildComicsState()

        val binding = FragmentComicsBinding.bind(view).apply {
            recycler.adapter = adapter
        }
        binding.toolbar.btImage.setOnClickListener { findNavController().popBackStack() }
        binding.toolbar.tvTitleToolbar.text = getString(R.string.title_toolbar_comics)
        viewLifecycleOwner.launchAndCollect(viewModel.state) {
            binding.loading = it.loading
            binding.comics = it.marvelComics
            binding.error = it.error?.let(comicsState::errorToString)
            binding.dateComics = it.dateComics
        }
        viewModel.onUiReady()
    }
}
