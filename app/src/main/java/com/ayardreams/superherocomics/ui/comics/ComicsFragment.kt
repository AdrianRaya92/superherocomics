package com.ayardreams.superherocomics.ui.comics

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ayardreams.superherocomics.R
import com.ayardreams.superherocomics.data.alert
import com.ayardreams.superherocomics.databinding.FragmentComicsBinding
import com.ayardreams.superherocomics.ui.common.launchAndCollect
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ComicsFragment : Fragment(R.layout.fragment_comics) {

    private val viewModel: ComicsViewModel by viewModels()
    private lateinit var comicsState: ComicsState
    private val adapter = ComicsAdapter {
        comicsState.onComicClicked(it)
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
            binding.totalComics = it.totalComics
        }

        comicsState.requestWriteExternalPermission { permission ->
            if (permission) {
                viewModel.onUiReady()
            } else {
                showGoToSettingsDialog()
            }
        }
    }

    private val settingsResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        findNavController().popBackStack()
    }

    private fun showGoToSettingsDialog() {
        alert {
            setTitle(R.string.text_alert_title_permission_storage)
            setMessage(R.string.text_alert_message_permission_storage)
            setPositiveButton(R.string.text_alert_positive_button_permission_storage) { _, _ ->
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                val uri = Uri.fromParts("package", requireActivity().packageName, null)
                intent.data = uri
                settingsResultLauncher.launch(intent)
            }
            setNegativeButton(R.string.common_cancel) { _, _ ->
                findNavController().popBackStack()
            }
        }.show()
    }
}
