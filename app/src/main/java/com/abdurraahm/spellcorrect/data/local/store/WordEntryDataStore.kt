package com.abdurraahm.spellcorrect.data.local.store

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.abdurraahm.spellcorrect.data.local.model.Section
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WordEntryDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    // DataStore instance
    private val Context.wordHistoryDataStore: DataStore<Preferences> by preferencesDataStore(name = "word_history")
    private fun wordHistoryKeyForSection(section: Section): Preferences.Key<String> {
        return stringPreferencesKey("word_history_${section.name}")
    }

    // Function to save a word to history for a specific section
    suspend fun saveWordToHistory(word: String, section: Section) {
        context.wordHistoryDataStore.edit { preferences ->
            preferences[wordHistoryKeyForSection(section)] = word
        }
    }

    // Function to retrieve word history for a specific section
    fun wordHistoryForSection(section: Section): Flow<String> {
        return context.wordHistoryDataStore.data
            .catch { exception ->
                Log.e("WordEntry Data Store", "$exception")
                // Handle exceptions here (e.g., emit an empty string)
                emit(emptyPreferences())
            }
            .map { preferences ->
                preferences[wordHistoryKeyForSection(section)] ?: ""
            }
    }
}