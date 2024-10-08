package com.abdurraahm.spellcorrect.data.repository

import android.speech.tts.TextToSpeech
import com.abdurraahm.spellcorrect.data.local.model.Section
import com.abdurraahm.spellcorrect.data.local.model.SectionData
import com.abdurraahm.spellcorrect.data.local.model.WordEntry
import com.abdurraahm.spellcorrect.data.service.SpeechToTextManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface MainRepository {
    // Speech To Text
    fun speechToTextManager(): SpeechToTextManager

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
    fun exerciseSpecific(word: String, section: Section): WordEntry
    fun wordOfTheDay(): Flow<WordEntry>
    fun getSectionListSize(section: Section): Flow<Int>
    fun exerciseStart(section: Section): Flow<List<WordEntry>>
    fun exerciseResume(section: Section): Flow<List<WordEntry>>
    suspend fun exerciseEnd(section: Section, currentIndex: Int)
    suspend fun nextLastIndexed(section: Section)
    suspend fun previousLastIndexed(section: Section)
    fun getLastIndexed(section: Section): Flow<Int>
    suspend fun saveLastIndexed(index: Int, section: Section)
    fun reviewSpeak(section: Section): Flow<List<WordEntry>>
    fun reviewListen(section: Section): Flow<List<WordEntry>>

    // Room Database
    suspend fun initDB(): Result<Boolean>
    fun totalSectionInDB(): Int
    fun isDBEmpty(): Flow<Boolean>
    fun sectionInDB(): Flow<List<SectionData>>
    fun getSectionDataById(id: Int): Flow<SectionData>
    suspend fun updateSectionDataId(sectionId: Int, newProgress: Float, newShownWordSet: Set<Int>)
}

