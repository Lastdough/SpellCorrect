package com.abdurraahm.spellcorrect.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.abdurraahm.spellcorrect.data.local.model.Section
import com.abdurraahm.spellcorrect.data.local.model.WordEntry
import kotlinx.coroutines.flow.Flow


@Dao
interface WordEntryDao {
    @Query("SELECT count(id) FROM word_entry WHERE section  =:section")
    fun wordEntryBySectionSize(section: Section): Flow<Int>

    @Query("SELECT count(id) FROM word_entry")
    fun wordEntrySize(): Flow<Int>

    @Query("SELECT * FROM word_entry WHERE section  =:section")
    fun wordEntryBySection(section: Section): Flow<List<WordEntry>>

    @Query("SELECT * FROM word_entry")
    fun wordEntryInDB(): Flow<List<WordEntry>>

    @Query("SELECT * FROM word_entry WHERE id = :id")
    fun getWordEntryByWord(id: Int): Flow<WordEntry>

    @Insert
    suspend fun insertWordEntryList(wordEntry: List<WordEntry>)

    @Insert
    suspend fun insertWordEntry(wordEntry: WordEntry)
}