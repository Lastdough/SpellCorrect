package com.abdurraahm.spellcorrect.data.local.source

import android.content.Context
import android.util.Log
import com.abdurraahm.spellcorrect.data.local.model.Section
import com.abdurraahm.spellcorrect.data.local.model.WordEntry
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import javax.inject.Inject

class WordEntryDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private lateinit var firstSectionJsonString: String
    private lateinit var secondSectionJsonString: String
    private lateinit var thirdSectionJsonString: String


    init {
        try {
            firstSectionJsonString = context.assets.open("separate/first_section.json")
                .bufferedReader()
                .use { it.readText() }
            secondSectionJsonString = context.assets.open("separate/second_section.json")
                .bufferedReader()
                .use { it.readText() }
            thirdSectionJsonString = context.assets.open("separate/third_section.json")
                .bufferedReader()
                .use { it.readText() }
        } catch (ioException: IOException) {
            Log.d("Data Source", "readWordEntriesFromAssets: $ioException ")
        }

    }

    fun sectionEntry(section: Section): List<WordEntry> {
        val listType = object : TypeToken<List<WordEntry>>() {}.type
        when (section) {
            Section.FIRST -> {
                val firstSection =
                    Gson().fromJson<List<WordEntry>>(firstSectionJsonString, listType)
                return firstSection
            }

            Section.SECOND -> {
                val secondSection =
                    Gson().fromJson<List<WordEntry>>(secondSectionJsonString, listType)
                return secondSection

            }

            Section.THIRD -> {
                val thirdSection =
                    Gson().fromJson<List<WordEntry>>(thirdSectionJsonString, listType)
                return thirdSection
            }
        }
    }

    fun mergedEntry(): List<WordEntry> {
        val listOfSection = listOf(
            sectionEntry(Section.FIRST),
            sectionEntry(Section.SECOND),
            sectionEntry(Section.THIRD)
        )
        val mergedSection = listOfSection.flatten()
        return mergedSection
    }

}