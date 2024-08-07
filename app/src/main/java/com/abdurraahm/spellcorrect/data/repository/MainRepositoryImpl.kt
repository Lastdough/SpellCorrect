package com.abdurraahm.spellcorrect.data.repository

import android.os.Build
import com.abdurraahm.spellcorrect.data.local.model.Section
import com.abdurraahm.spellcorrect.data.local.model.WordEntry
import com.abdurraahm.spellcorrect.data.local.source.NavigationDataStore
import com.abdurraahm.spellcorrect.data.local.source.WordEntryDataSource
import com.abdurraahm.spellcorrect.data.local.source.WordEntryDataStore
import kotlinx.coroutines.flow.Flow
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
    private val wordEntryDataSource: WordEntryDataSource,
    private val wordEntryDataStore: WordEntryDataStore,
    private val navigationDataStore: NavigationDataStore
) : MainRepository {

    // Navigation
    override val onboardingState: Flow<Boolean> = navigationDataStore.onboardingState
    override suspend fun updateOnboardingState(completed: Boolean) {
        navigationDataStore.updateOnboardingState(completed)
    }

    // Word Entry
    override fun wordOfTheDay(): WordEntry {
        val wordEntry = wordEntryDataSource.mergedEntry()
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
        return wordOfTheDay
    }

    override fun exerciseStart(section: Section): List<WordEntry> {
        return wordEntryDataSource.sectionEntry(section)
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
