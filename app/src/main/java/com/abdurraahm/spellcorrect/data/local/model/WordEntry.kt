package com.abdurraahm.spellcorrect.data.local.model

data class WordEntry(
    val word: String,
    val definition: List<String>,
    val type: String,
    val ipa: String
) {
    private fun String.capitalizeFirstLetter(): String {
        return if (this.isNotEmpty()) {
            this.substring(0, 1).uppercase() + this.substring(1)
        } else {
            this
        }
    }

    val wordFirstLetterCapitalized
        get() = word.capitalizeFirstLetter()

    private fun String.formatType(): String = if (arrayOf(
            'a',
            'i',
            'u',
            'e',
            'o'
        ).contains(this.first())
    ) "an $this" else "a $this"

    private fun List<String>.formatDefinitions(): String =
        this.mapIndexed { index, definition ->
            when (index) {
                0 -> definition
                1 -> "or $definition"
                2 -> "or $definition"
                else -> "Another meaning is: $definition"
            }
        }.joinToString(separator = ". ") { it }

    private fun List<String>.mergeDefinitions(): String =
        this.mapIndexed { index, definition ->
            "${index + 1}. $definition"
        }.joinToString(separator = "\n") { it }

    val definitionWithNumber
        get() = definition.mergeDefinitions()

    val fullDescription
        get() = "$word: ${type.formatType()} meaning ${definition.formatDefinitions()}"

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
