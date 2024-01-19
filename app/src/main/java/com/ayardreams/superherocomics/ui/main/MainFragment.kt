package com.ayardreams.superherocomics.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.ayardreams.superherocomics.R
import com.ayardreams.superherocomics.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        FragmentMainBinding.bind(view).apply {
            btnNewComics.setOnClickListener { findNavController().navigate(R.id.action_main_to_comics) }
            btnReaderComics.setOnClickListener { findNavController().navigate(R.id.action_main_to_reader_qr) }
        }
    }
}
