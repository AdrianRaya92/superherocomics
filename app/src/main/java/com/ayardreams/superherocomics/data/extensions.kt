package com.ayardreams.superherocomics.data

import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

inline fun <reified T : ViewBinding> Fragment.viewBinding() = FragmentViewBindingDelegate(this, T::class.java)

fun Fragment.alert(dialogBuilder: AlertDialog.Builder.() -> Unit): AlertDialog {
    val builder = AlertDialog.Builder(requireContext())
    builder.dialogBuilder()
    return builder.create()
}