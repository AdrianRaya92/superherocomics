package com.ayardreams.superherocomics.data

import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import arrow.core.Either
import arrow.core.left
import arrow.core.right
import com.ayardreams.domain.Error
import retrofit2.HttpException
import java.io.IOException

fun Fragment.alert(dialogBuilder: AlertDialog.Builder.() -> Unit): AlertDialog {
    val builder = AlertDialog.Builder(requireContext())
    builder.dialogBuilder()
    return builder.create()
}

fun Throwable.toError(): Error = when (this) {
    is IOException -> Error.Connectivity
    is HttpException -> Error.Server(code())
    else -> Error.Unknown(message ?: "")
}

suspend fun <T> tryCall(action: suspend () -> T): Either<Error, T> = try {
    action().right()
} catch (e: Exception) {
    e.toError().left()
}
