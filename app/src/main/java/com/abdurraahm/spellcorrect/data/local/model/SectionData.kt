package com.abdurraahm.spellcorrect.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "section_table")
data class SectionData(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val description: String,
    val progress: Float,
) {
    private fun Float.toPercent(): String {
        return "%.0f".format(this * 100)
    }

    private fun Int.toRomanNumeral(): String {
        val map = listOf(
            1000 to "M", 900 to "CM", 500 to "D", 400 to "CD",
            100 to "C", 90 to "XC", 50 to "L", 40 to "XL",
            10 to "X", 9 to "IX", 5 to "V", 4 to "IV", 1 to "I"
        )

        var num = this
        val romanNumeral = StringBuilder()

        for ((value, symbol) in map) {
            while (num >= value) {
                romanNumeral.append(symbol)
                num -= value
            }
        }

        return romanNumeral.toString()
    }

    val partInArabicNumeral
        get() = id + 1

    val partSection
        get() = Section.entries[id]

    val partInRomanNumeral
        get() = partInArabicNumeral.toRomanNumeral()

    val progressInPercent
        get() = progress.toPercent()

    val finished: Boolean
        get() = progress >= 1F

    val started: Boolean
        get() = progress > 0F
}