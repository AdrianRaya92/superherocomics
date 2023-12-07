package com.ayardreams.superherocomics.ui.common

import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class PermissionRequester(fragment: Fragment, private val permission: String) {

    private var onGranted: (Boolean) -> Unit = {}
    private var onDenied: () -> Unit = {}
    private val launcher =
        fragment.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                onGranted(isGranted)
            } else {
                onDenied()
            }
        }

    suspend fun request(onDenied: () -> Unit): Boolean =
        suspendCancellableCoroutine { continuation ->
            this.onGranted = {
                continuation.resume(it)
            }
            this.onDenied = onDenied
            launcher.launch(permission)
        }
}