package com.abdurraahm.spellcorrect.data.repository

import com.abdurraahm.spellcorrect.data.local.model.Section
import com.abdurraahm.spellcorrect.data.local.model.WordEntry
import com.abdurraahm.spellcorrect.data.local.source.WordEntryDataSource
import com.abdurraahm.spellcorrect.data.local.source.WordEntryDataStore
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WordEntryRepositoryImpl @Inject constructor(
    private val wordEntryDataSource: WordEntryDataSource,
    private val wordEntryDataStore: WordEntryDataStore
) : WordEntryRepository {
    override fun wordOfTheDay(): WordEntry {
        return wordEntryDataSource.sectionEntry(Section.FIRST)[0]
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