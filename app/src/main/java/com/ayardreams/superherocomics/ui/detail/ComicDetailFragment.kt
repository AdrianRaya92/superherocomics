package com.ayardreams.superherocomics.ui.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ayardreams.superherocomics.R
import com.ayardreams.superherocomics.databinding.FragmentComicDetailBinding
import com.ayardreams.superherocomics.ui.common.launchAndCollect
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ComicDetailFragment : Fragment(R.layout.fragment_comic_detail) {

    private val viewModel: ComicDetailViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentComicDetailBinding.bind(view)

        binding.toolbar.btImage.setOnClickListener { findNavController().popBackStack() }

        viewLifecycleOwner.launchAndCollect(viewModel.state) { state ->
            if (state.comic != null) {
                binding.comic = state.comic
                binding.toolbar.tvTitleToolbar.text = state.comic.title
            }
        }
    }
}