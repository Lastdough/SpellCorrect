package com.abdurraahm.spellcorrect.data.repository

import com.abdurraahm.spellcorrect.data.local.model.Section
import com.abdurraahm.spellcorrect.data.local.model.WordEntry

interface WordEntryRepository {
    fun wordOfTheDay(): WordEntry
    fun exerciseStart(section: Section): List<WordEntry>
    fun exerciseResume(section: Section): List<WordEntry>
    fun exerciseSpecific(word: String, section: Section): WordEntry
    fun reviewType(section: Section): List<WordEntry>
    fun reviewListen(section: Section): List<WordEntry>
}

