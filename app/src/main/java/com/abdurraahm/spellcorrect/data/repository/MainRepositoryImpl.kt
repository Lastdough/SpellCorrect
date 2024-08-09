package com.abdurraahm.spellcorrect.data.repository

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import com.abdurraahm.spellcorrect.data.local.model.Section
import com.abdurraahm.spellcorrect.data.local.model.WordEntry
import com.abdurraahm.spellcorrect.data.local.store.NavigationDataStore
import com.abdurraahm.spellcorrect.data.local.source.WordEntryLocalDataSource
import com.abdurraahm.spellcorrect.data.local.store.WordEntryDataStore
import com.abdurraahm.spellcorrect.data.service.TextToSpeechService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class MainRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val wordEntryLocalDataSource: WordEntryLocalDataSource,
    private val wordEntryDataStore: WordEntryDataStore,
    private val navigationDataStore: NavigationDataStore,
    private val ttsService: TextToSpeechService
) : MainRepository {
    // Text To Speech
    override fun startTextToSpeech() {
        val intent = Intent(context, TextToSpeechService::class.java)
        context.startService(intent)
    }

    override fun stopTextToSpeech() {
        val intent = Intent(context, TextToSpeechService::class.java)
        context.stopService(intent)
    }

    override val tts: TextToSpeech
        get() = ttsService.tts

    private val _rate = MutableStateFlow(0.95f)
    override val rate: StateFlow<Float> = _rate.asStateFlow()

    override fun updateRate(newRate: Float) {
        _rate.value = newRate.coerceIn(0.1f, 2.0f)
    }

    override fun speak(text: String) {
        val queueMode: Int = TextToSpeech.QUEUE_FLUSH
        tts.language = Locale.ENGLISH
        tts.setPitch(1f)
        tts.setSpeechRate(rate.value)
        val voice = tts.voices.find { v -> v.name == "en-US-language" } ?: tts.defaultVoice
        tts.setVoice(voice)
        tts.speak(text, queueMode, Bundle(), "utteranceId")
    }


    // Navigation
    override val onboardingState: Flow<Boolean> = navigationDataStore.onboardingState
    override suspend fun updateOnboardingState(completed: Boolean) {
        navigationDataStore.updateOnboardingState(completed)
    }

 // Word Entry
override fun wordOfTheDay(): Flow<WordEntry> = flow {
    val wordEntry = wordEntryLocalDataSource.mergedEntry()// Collect the list of entries
    val seed = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        // Using newer APIs for Oreo and above
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("ddMMyyyy")
        "50002101" + currentDate.format(formatter)
    } else {
        // Fallback for older versions
        val currentDateOlder = Date()
        val formatterOlder = SimpleDateFormat("ddMMyyyy", Locale.getDefault())
        "50002101" + formatterOlder.format(currentDateOlder)
    }
    val randomSeeded = Random(seed.toLong())
    val wordOfTheDayIndex = wordEntry.indices.random(randomSeeded)
    val wordOfTheDay = wordEntry[wordOfTheDayIndex]
    emit(wordOfTheDay) // Emit the selected WordEntry
}

    override fun exerciseStart(section: Section): Flow<List<WordEntry>> {
        return wordEntryLocalDataSource.sectionEntry(section)
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
