package com.abdurraahm.spellcorrect.data.repository

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.tts.TextToSpeech
import com.abdurraahm.spellcorrect.data.local.dao.SectionDataDao
import com.abdurraahm.spellcorrect.data.local.model.Section
import com.abdurraahm.spellcorrect.data.local.model.SectionData
import com.abdurraahm.spellcorrect.data.local.model.WordEntry
import com.abdurraahm.spellcorrect.data.local.source.WordEntryLocalDataSource
import com.abdurraahm.spellcorrect.data.local.store.NavigationDataStore
import com.abdurraahm.spellcorrect.data.local.store.ProgressDataStore
import com.abdurraahm.spellcorrect.data.local.store.WordEntryDataStore
import com.abdurraahm.spellcorrect.data.service.SeedGenerator
import com.abdurraahm.spellcorrect.data.service.TextToSpeechService
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.util.Collections
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
    private val progressDataStore: ProgressDataStore,
    private val ttsService: TextToSpeechService,
    private val sectionDataDao: SectionDataDao,
    private val seedGenerator: SeedGenerator
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
        // Collect the list once
        val wordList = wordEntryLocalDataSource.mergedSection.first()
        while (true) {
            val randomSeeded = Random(seedGenerator.generateDaily())
            val wordOfTheDayIndex = wordList.indices.random(randomSeeded)
            val wordOfTheDay = wordList[wordOfTheDayIndex]
            emit(wordOfTheDay)
        }
    }.flowOn(Dispatchers.IO)

    override fun exerciseSpecific(word: String, section: Section): WordEntry {
        TODO("Not yet implemented")
    }

    override fun exerciseStart(section: Section): Flow<List<WordEntry>> = flow {
        val seed = seedGenerator.generate()
        progressDataStore.saveSeed(seed = seed, section = section)
        progressDataStore.saveLastIndex(index = 0, section = section)
        val randomSeeded = Random(seed)
        val list = wordEntryLocalDataSource.sectionEntry(section).first()
        val shuffledList = list.shuffled(randomSeeded)
        emit(shuffledList)
    }

    /**
     * Collection.rotate but my implementation
     *
     * var shuffledList = list.shuffled(randomSeeded)
     *
     * val firstHalf = shuffledList.subList(0, lastIndex - 1)
     *
     * val secondHalf = shuffledList.subList(lastIndex, shuffledList.lastIndex)
     *
     * shuffledList = secondHalf + firstHalf
     */
    override suspend fun exerciseEnd(section: Section, currentIndex: Int) =
        progressDataStore.saveLastIndex(index = currentIndex, section = section)

    override fun exerciseResume(section: Section): Flow<List<WordEntry>> = flow {
        val seed = progressDataStore.getLastSeed(section).first()
        val lastIndex = progressDataStore.getLastIndex(section).first()
        val randomSeeded = Random(seed)
        val list = wordEntryLocalDataSource.sectionEntry(section).first()
        val shuffledList = list.shuffled(randomSeeded)
        Collections.rotate(shuffledList, -lastIndex)
        emit(shuffledList)
    }

    override fun reviewType(section: Section): List<WordEntry> {
        TODO("Not yet implemented")
    }

    override fun reviewListen(section: Section): List<WordEntry> {
        TODO("Not yet implemented")
    }

    // Room Database
    override fun totalSectionInDB() =
        sectionDataDao.totalSectionInDB()

    override fun sectionInDB(): Flow<List<SectionData>> =
        sectionDataDao.dataInDB()

    override fun getSectionDataById(id: Int): Flow<SectionData> =
        sectionDataDao.getSectionDataById(id)

    override suspend fun updateSectionData(sectionData: SectionData) =
        sectionDataDao.updateSectionData(sectionData)
}
