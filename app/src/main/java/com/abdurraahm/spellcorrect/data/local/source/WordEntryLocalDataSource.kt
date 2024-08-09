package com.abdurraahm.spellcorrect.data.local.source

import android.content.Context
import android.util.Log
import com.abdurraahm.spellcorrect.data.local.model.Section
import com.abdurraahm.spellcorrect.data.local.model.WordEntry
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import java.util.Locale
import javax.inject.Inject

class WordEntryLocalDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val sectionJsonStrings = mutableMapOf<Section, String>()

    init {
        try {
            Section.entries.forEach { section ->
                sectionJsonStrings[section] = context.assets.open("separate/${
                    section.name.lowercase(
                        Locale.ROOT
                    )
                }_section.json")
                    .bufferedReader()
                    .use { it.readText() }
            }
        } catch (ioException: IOException) {
            Log.d("Data Source", "Error loading section data: $ioException")
        }
    }

    fun sectionEntry(section: Section): List<WordEntry> {
        val listType = object : TypeToken<List<WordEntry>>() {}.type
        return sectionJsonStrings[section]?.let {
            Gson().fromJson(it, listType)
        } ?: emptyList()
    }

    fun mergedEntry(): List<WordEntry> {
        return Section.entries.flatMap { sectionEntry(it) }
    }

}