package com.abdurraahm.spellcorrect.data.repository

import android.speech.tts.TextToSpeech
import com.abdurraahm.spellcorrect.data.local.model.Section
import com.abdurraahm.spellcorrect.data.local.model.SectionData
import com.abdurraahm.spellcorrect.data.local.model.WordEntry
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface MainRepository {
    // Text To Speech
    fun startTextToSpeech()
    fun stopTextToSpeech()

    val tts: TextToSpeech
    val rate: StateFlow<Float>
    fun updateRate(newRate: Float)
    fun speak(text: String)

    // Navigation
    val onboardingState: Flow<Boolean>
    suspend fun updateOnboardingState(completed: Boolean)

    // Word Entry
    fun wordOfTheDay(): Flow<WordEntry>
    fun exerciseStart(section: Section): Flow<List<WordEntry>>
    fun exerciseResume(section: Section): List<WordEntry>
    fun exerciseSpecific(word: String, section: Section): WordEntry
    fun reviewType(section: Section): List<WordEntry>
    fun reviewListen(section: Section): List<WordEntry>

    // Room Database
    fun totalSectionInDB(): Int
    fun sectionInDB(): Flow<List<SectionData>>
    fun getSectionDataById(id: Int): Flow<SectionData>
    suspend fun updateSectionData(sectionData: SectionData)
}

