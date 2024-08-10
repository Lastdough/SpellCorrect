package com.abdurraahm.spellcorrect.data.service

import android.os.Build
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import kotlin.random.Random

class SeedGenerator @Inject constructor() {
    private fun sha256Hash(s: String): Long {
        val bytes = MessageDigest.getInstance("SHA-256").digest(s.toByteArray())
        return bytes.fold(0L) { hash, byte ->
            hash * 31 + byte.toLong()
        } and Long.MAX_VALUE // Keep it within the signed 64-bit range
    }

    fun generate(): Long = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        // Using newer APIs for Oreo and above
        val currentDate = LocalDateTime.now() // Use LocalDateTime for milliseconds
        val formatter = DateTimeFormatter.ofPattern("ddMMyyHHmmssSSS") // Include milliseconds
        sha256Hash("50002101" + currentDate.format(formatter))
    } else {
        // Fallback for older versions
        val currentDateOlder = Date()
        val formatterOlder =
            SimpleDateFormat("ddMMyyyyHHmmssSSS", Locale.getDefault()) // Include milliseconds
        sha256Hash("50002101" + formatterOlder.format(currentDateOlder))
    }

    fun generateDaily(): Long = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        // Using newer APIs for Oreo and above
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("ddMMyyyy")
        sha256Hash("50002101" + currentDate.format(formatter))
    } else {
        // Fallback for older versions
        val currentDateOlder = Date()
        val formatterOlder = SimpleDateFormat("ddMMyyyy", Locale.getDefault())
        sha256Hash("50002101" + formatterOlder.format(currentDateOlder))
    }

    fun randomSeeded() = Random(generate())
}

