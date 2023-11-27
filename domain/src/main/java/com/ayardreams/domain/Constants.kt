package com.ayardreams.domain

import java.security.MessageDigest

interface Constants

object MarvelApi : Constants {
    const val publicKey = "fb3d425398487ce844ab6b837617f5f4"
    const val limit = 100
    private const val privateKey = "e186da24da7031dce65b1837efa56cbb006ce83a"
    var ts = System.currentTimeMillis().toString()
    var hash = generateHash(ts, publicKey, privateKey)
}

fun generateHash(ts: String, publicKey: String, privateKey: String): String {
    val input = "$ts$privateKey$publicKey"
    val md = MessageDigest.getInstance("MD5")
    val digest = md.digest(input.toByteArray())

    val hexString = StringBuilder()
    for (byte in digest) {
        hexString.append(String.format("%02x", byte))
    }

    return hexString.toString()
}