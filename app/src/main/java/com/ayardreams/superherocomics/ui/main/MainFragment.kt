package com.ayardreams.superherocomics.ui.main

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.ayardreams.superherocomics.R
import com.ayardreams.superherocomics.data.viewBinding
import com.ayardreams.superherocomics.databinding.FragmentMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainFragment : Fragment(R.layout.fragment_main) {

    private val binding by viewBinding<FragmentMainBinding>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            btnCharacters.setOnClickListener { //findNavController().navigate(R.id) }
                btnComics.setOnClickListener { }
                btnShopLocation.setOnClickListener { }
            }
        }
    }
}