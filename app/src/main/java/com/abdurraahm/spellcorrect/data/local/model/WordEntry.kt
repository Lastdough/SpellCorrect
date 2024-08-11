package com.abdurraahm.spellcorrect.data.local.model

data class WordEntry(
    val word: String,
    val definition: List<String>,
    val type: String,
    val ipa: String
) {
    private fun String.capitalizeFirstLetter() =
        if (isNotEmpty()) substring(0, 1).uppercase() + substring(1) else this

    val wordFirstLetterCapitalized
        get() = word.capitalizeFirstLetter()

    private fun String.formatType() = when {
        isEmpty() -> ""
        first() in "aiueo" -> "an $this"
        else -> "a $this"
    }

    private fun List<String>.formatDefinitions() =
        mapIndexed { index, definition ->
            when (index) {
                0 -> definition
                1, 2 -> "or $definition"
                else -> "Another meaning is: $definition"
            }
        }.joinToString(separator = ". ") { it }

    private fun List<String>.mergeDefinitions() =
        mapIndexed { index, definition ->
            "${index + 1}  : $definition"
        }.joinToString(separator = "\n") { it }

    val definitionWithNumber
        get() = definition.mergeDefinitions()

    val fullDescription
        get() = "$word: ${type.clean().formatType()} meaning ${definition.formatDefinitions()}"

    private fun String.abbreviation() = when (this) {
        "noun" -> "Noun"
        "geographical name" -> "Geo"
        "adjective" -> "Adj"
        "verb" -> "Verb"
        "adverb" -> "Adv"
        "interjection" -> "Interj"
        "conjunction" -> "Conj"
        "preposition" -> "Prep"
        "trademark" -> "TM"
        "combining form" -> "Comb"
        "service mark" -> "SM"
        else -> ""
    }

    private fun String.clean() =
        if (this == "No word type available") ""
        else this

    private fun String.capital() = when (this) {
        "noun", "adjective", "verb", "adverb", "interjection", "conjunction", "preposition", "trademark" -> capitalizeFirstLetter()
        "combining form" -> "Combining Form"
        "geographical name" -> "Geographical Name"
        "service mark" -> "Service Mark"
        else -> ""
    }

    // Get the abbreviation and cleaned values for the type property
    val typeAbbreviation: String
        get() = type.abbreviation()

    val typeClean: String
        get() = type.clean().capital()

    val hasIPA: Boolean
        get() = ipa != "No IPA available"

}
