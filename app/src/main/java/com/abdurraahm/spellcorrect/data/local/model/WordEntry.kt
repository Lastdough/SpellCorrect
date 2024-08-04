package com.abdurraahm.spellcorrect.data.local.model

data class WordEntry(
    val word: String,
    val definition: List<String>,
    val type: String,
    val ipa: String
)
