package com.abdurraahm.spellcorrect.data.local.model

import com.abdurraahm.spellcorrect.ui.utils.capitalizeFirstLetter

data class WordEntry(
    val word: String,
    val definition: List<String>,
    val type: String,
    val ipa: String
) {

    private fun String.abbreviation(): String {
        return when (this) {
            "noun" -> "Noun"
            "adjective" -> "Adj"
            "verb" -> "Verb"
            "adverb" -> "Adv"
            "interjection" -> "Interj"
            "conjunction" -> "Conj"
            "No word type available" -> ""
            "preposition" -> "Prep"
            "trademark" -> "TM"
            "combining form" -> "Comb"
            "service mark" -> "SM"
            else -> ""
        }
    }

    private fun String.clean(): String {
        return when (this) {
            "noun", "adjective", "verb", "adverb", "interjection", "conjunction", "preposition", "trademark" -> this.capitalizeFirstLetter()
            "combining form" -> "Combining Form"
            "service mark" -> "Service Mark"
            "No word type available" -> ""
            else -> ""
        }
    }

    // Get the abbreviation and cleaned values for the type property
    val typeAbbreviation: String
        get() = type.abbreviation()

    val typeClean: String
        get() = type.clean()

}
