package com.ayardreams.superherocomics.ui.detail

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ayardreams.superherocomics.R
import com.ayardreams.superherocomics.data.alert
import com.ayardreams.superherocomics.databinding.FragmentComicDetailBinding
import com.ayardreams.superherocomics.ui.common.launchAndCollect
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ComicDetailFragment : Fragment(R.layout.fragment_comic_detail) {

    private val viewModel: ComicDetailViewModel by viewModels()
    private lateinit var comicsDetailsState: ComicsDetailsState
    private lateinit var dataQr: String
    private lateinit var titleComic: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        comicsDetailsState = buildComicsDetailsState()

        val binding = FragmentComicDetailBinding.bind(view)

        with(binding) {
            toolbar.btImage.setOnClickListener { findNavController().popBackStack() }
            btnGenerateQrCode.setOnClickListener {
                qrCodeImageView.setImageBitmap(viewModel.generateQRCode(dataQr))
                btnGenerateQrCode.visibility = View.GONE
                btnSaveQR.visibility = View.VISIBLE
            }
            btnSaveQR.setOnClickListener {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
                    viewModel.saveImage(titleComic)
                } else {
                    viewModel.saveImage(requireContext().contentResolver, titleComic)
                }
                btnSaveQR.visibility = View.GONE
            }
        }

        viewLifecycleOwner.launchAndCollect(viewModel.state) { state ->
            if (state.comic != null) {
                binding.comic = state.comic
                binding.toolbar.tvTitleToolbar.text = state.comic.title
                titleComic = state.comic.title
                dataQr = "${getString(R.string.app_name)};" +
                    "T:${state.comic.title};" +
                    "R:${state.comic.resume};" +
                    "N:${state.comic.issueNumber};" +
                    "P:${state.comic.price};" +
                    "C:${state.comic.pageCount};" +
                    "I:${state.comic.thumbnail};"
            }
            if (state.qrSave) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.text_save_qr_image, state.messageStorage),
                    Toast.LENGTH_LONG
                ).show()
            }
            state.error?.let { error ->
                alert {
                    state.error.toString()
                    setMessage(error.let(comicsDetailsState::errorToString))
                    setPositiveButton(android.R.string.ok) { _, _ -> }
                }.show()
            }
        }
    }
}
