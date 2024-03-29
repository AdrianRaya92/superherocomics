package com.ayardreams.superherocomics.ui.reader

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.ayardreams.domain.ReaderComics
import com.ayardreams.superherocomics.R
import com.ayardreams.superherocomics.data.alert
import com.ayardreams.superherocomics.databinding.FragmentComicReaderBinding
import com.ayardreams.superherocomics.ui.common.launchAndCollect
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.google.zxing.integration.android.IntentIntegrator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ComicReaderFragment : Fragment(R.layout.fragment_comic_reader) {
    private val viewModel: ComicReaderViewModel by viewModels()
    private lateinit var comicsReaderState: ComicsReaderState

    private var _binding: FragmentComicReaderBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentComicReaderBinding.bind(view)
        comicsReaderState = buildComicsReaderState()

        binding.toolbar.btImage.setOnClickListener { findNavController().popBackStack() }
        binding.toolbar.tvTitleToolbar.text = getString(R.string.title_toolbar_reader)
        binding.btnStartReader.setOnClickListener { initScanner() }
        binding.btnReaderQrCode.setOnClickListener { initScanner() }

        comicsReaderState.requestCameraPermission { permission ->
            if (!permission) {
                showGoToSettingsDialog()
            }
        }

        viewLifecycleOwner.launchAndCollect(viewModel.state) {
            binding.comic = it.readerComics
        }
    }

    private val settingsResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        findNavController().popBackStack()
    }

    private fun initScanner() {
        val integrator = IntentIntegrator.forSupportFragment(this)
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        integrator.setPrompt("ESCANEAR QR SUPERHERO COMICS")
        integrator.setBeepEnabled(false)
        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if (result != null) {
            if (result.contents == null) {
                Snackbar
                    .make(
                        binding.lyReader,
                        getString(R.string.common_canceled_scan_qr),
                        BaseTransientBottomBar.LENGTH_LONG
                    )
                    .show()
            } else {
                if (result.formatName.equals("QR_CODE")
                    && result.contents.startsWith(getString(R.string.app_name) + ";T:")) {
                    viewModel.onUiReady(
                        ReaderComics(
                            result.contents.substringAfter(";T:").substringBefore(";R:"),
                            result.contents.substringAfter(";R:").substringBefore(";N:"),
                            result.contents.substringAfter(";N:").substringBefore(";P:"),
                            result.contents.substringAfter(";P:").substringBefore(";C:"),
                            result.contents.substringAfter(";C:").substringBefore(";I:"),
                            result.contents.substringAfter(";I:").substringBefore(";")
                        )
                    )

                    Snackbar
                        .make(
                            binding.lyReader,
                            getString(R.string.common_scan_qr),
                            BaseTransientBottomBar.LENGTH_LONG
                        )
                        .show()
                } else {
                    Snackbar
                        .make(
                            binding.lyReader,
                            getString(R.string.common_error_scan_qr),
                            BaseTransientBottomBar.LENGTH_LONG
                        )
                        .show()
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun showGoToSettingsDialog() {
        alert {
            setTitle(R.string.text_alert_title_permission_camera)
            setMessage(R.string.text_alert_message_permission_camera)
            setPositiveButton(R.string.text_alert_positive_button_permission_camera) { _, _ ->
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
