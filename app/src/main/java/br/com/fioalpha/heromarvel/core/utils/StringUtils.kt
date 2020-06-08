package br.com.fioalpha.heromarvel.core.utils

import java.security.MessageDigest
import java.util.Date

fun String.convertToMD5() = MessageDigest.getInstance("MD5")
    .digest(this.toByteArray())
    .joinToString("") { "%02x".format(it) }

class GeneratorHash(
    private val publicKey: String,
    private val privateKey: String,
    private val date: Date
) {

    fun getPublicKey() = publicKey

    fun getTimeStamp() = date.time

    fun createHash(): String {
        return createHash(
            date.time,
            publicKey,
            privateKey
        )
    }

    private fun createHash(
        time: Long,
        publicKey: String,
        privateKey:
        String
    ): String = "$time$privateKey$publicKey".convertToMD5()
}
