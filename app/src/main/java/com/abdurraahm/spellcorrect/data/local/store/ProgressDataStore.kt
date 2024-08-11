package com.abdurraahm.spellcorrect.data.local.store

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.abdurraahm.spellcorrect.data.local.model.Section
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ProgressDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    // Single DataStore instance for both seed and progress
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "progress_data")

    private fun seedKeyForSection(section: Section): Preferences.Key<String> {
        return stringPreferencesKey("${section.name}_seed")
    }

    suspend fun saveSeed(seed: Long, section: Section) {
        context.dataStore.edit { preferences ->
            preferences[seedKeyForSection(section)] = seed.toString()
        }
    }

    fun getLastSeed(section: Section): Flow<Long> {
        return context.dataStore.data
            .catch { exception ->
                Log.e("Progress Data Store", "$exception")
                emit(emptyPreferences())
            }
            .map { preferences ->
                preferences[seedKeyForSection(section)]?.toLong() ?: 0L
            }
    }

    private fun progressKeyForSection(section: Section): Preferences.Key<String> {
        return stringPreferencesKey("${section.name}_progress")
    }

    suspend fun saveLastIndexed(index: Int, section: Section) {
        context.dataStore.edit { preferences ->
            preferences[progressKeyForSection(section)] = index.toString()
        }
    }

    fun getLastIndexed(section: Section): Flow<Int> {
        return context.dataStore.data
            .catch { exception ->
                Log.e("Progress Data Store", "$exception")
                emit(emptyPreferences())
            }
            .map { preferences ->
                preferences[progressKeyForSection(section)]?.toInt() ?: 0
            }
    }

    private fun historyKeyForSection(section: Section): Preferences.Key<String> {
        return stringPreferencesKey("${section.name}_history")
    }

    suspend fun saveHistory(seenWords: Set<Int>, section: Section) {
        context.dataStore.edit { preferences ->
            val serializedSet = seenWords.joinToString(",") // Serialize the set to a string
            preferences[historyKeyForSection(section)] = serializedSet
        }
    }

    fun getHistory(section: Section): Flow<Set<Int>> {
        return context.dataStore.data
            .catch { exception ->
                Log.e("Progress Data Store", "$exception")
                emit(emptyPreferences())
            }
            .map { preferences ->
                val serializedSet = preferences[historyKeyForSection(section)] ?: ""
                if (serializedSet.isEmpty()) {
                    emptySet()
                } else {
                    serializedSet.split(",").map { it.toInt() }.toSet() // Deserialize the string to a set
                }
            }
    }

    private val wordHistoryCount = intPreferencesKey("words_shown_count")

    suspend fun saveWordsShownCount(count: Int, section: Section) {
        context.dataStore.edit { preferences ->
            preferences[wordHistoryCount] = count
        }
    }

    fun getWordsShownCount(section: Section): Flow<Int> {
        return context.dataStore.data
            .catch { exception ->
                Log.e("Progress Data Store", "$exception")
                emit(emptyPreferences())
            }
            .map { preferences ->
                preferences[wordHistoryCount] ?: 0
            }
    }
}