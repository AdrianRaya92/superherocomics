package com.ayardreams.superherocomics.ui.characters

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ayardreams.superherocomics.R
import com.ayardreams.superherocomics.databinding.FragmentCharactersBinding
import com.ayardreams.superherocomics.ui.common.launchAndCollect
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CharactersFragment : Fragment(R.layout.fragment_characters) {
    private val viewModel: CharactersViewModel by viewModels()
    private lateinit var characterState: CharacterState
    private val adapter = CharactersAdapter {
        //characterState.onMovieClicked(it)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        characterState = buildCharacterState()

        val binding = FragmentCharactersBinding.bind(view).apply {
            recycler.adapter = adapter
        }

        viewLifecycleOwner.launchAndCollect(viewModel.state) {
            binding.loading = it.loading
            binding.characters = it.marvelCharacters
            binding.error = it.error?.let(characterState::errorToString)
        }

        viewModel.onUiReady()
    }
}
