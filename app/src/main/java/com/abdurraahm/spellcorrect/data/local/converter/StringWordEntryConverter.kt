package com.abdurraahm.spellcorrect.data.local.converter

import com.abdurraahm.spellcorrect.data.local.model.Section
import com.abdurraahm.spellcorrect.data.local.model.WordEntry

object StringWordEntryConverter {
    fun convertToEmptyWordEntry(sectionId: Int, word: String) = WordEntry(
        id = 0,
        word = word,
        type = "",
        section = Section.entries[sectionId],
        definition = listOf(""),
        ipa = ""
    )
}