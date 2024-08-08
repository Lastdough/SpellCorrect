package com.abdurraahm.spellcorrect.data.repository

import com.abdurraahm.spellcorrect.data.local.model.Section
import com.abdurraahm.spellcorrect.data.local.model.WordEntry
import com.abdurraahm.spellcorrect.data.service.TextToSpeechService
import kotlinx.coroutines.flow.Flow

interface MainRepository {
    // Text To Speech
    fun startTextToSpeech()
    fun stopTextToSpeech()

    fun ttsService() : TextToSpeechService
    fun speak(text: String)

    // Navigation
    val onboardingState: Flow<Boolean>
    suspend fun updateOnboardingState(completed: Boolean)

    // Word Entry
    fun wordOfTheDay(): WordEntry
    fun exerciseStart(section: Section): List<WordEntry>
    fun exerciseResume(section: Section): List<WordEntry>
    fun exerciseSpecific(word: String, section: Section): WordEntry
    fun reviewType(section: Section): List<WordEntry>
    fun reviewListen(section: Section): List<WordEntry>
}

