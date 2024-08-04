package com.abdurraahm.spellcorrect.data.repository

import com.abdurraahm.spellcorrect.data.local.model.Section
import com.abdurraahm.spellcorrect.data.local.model.WordEntry
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WordEntryRepositoryImpl @Inject constructor() : WordEntryRepository {
    override fun wordOfTheDay(): WordEntry {
        return WordEntry(
            word = "Test 2",
            type = "Verb",
            ipa = "as",
            definition = listOf("This is A Test Word")
        )
    }

    override fun exerciseStart(section: Section): List<WordEntry> {
        TODO("Not yet implemented")
    }

    override fun exerciseResume(section: Section): List<WordEntry> {
        TODO("Not yet implemented")
    }

    override fun exerciseSpecific(word: String, section: Section): WordEntry {
        TODO("Not yet implemented")
    }

    override fun reviewType(section: Section): List<WordEntry> {
        TODO("Not yet implemented")
    }

    override fun reviewListen(section: Section): List<WordEntry> {
        TODO("Not yet implemented")
    }
}