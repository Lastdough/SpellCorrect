package com.abdurraahm.spellcorrect.data.local.source

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NavigationDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val Context.onboardingDataStore: DataStore<Preferences> by preferencesDataStore(name = "onboarding")
    private val onBoardingCompletedKey: Preferences.Key<Boolean> = booleanPreferencesKey("onboarding_completed")

    val onboardingState: Flow<Boolean>
        get() = context.onboardingDataStore.data
            .catch { exception ->
                // Handle exceptions (emit false if an error occurs)
                Log.e("Navigation Data Store", "$exception")
                emit(emptyPreferences())
            }
            .map{ preferences ->
                preferences[onBoardingCompletedKey] ?: false // Default to false if not found
            }

    suspend fun updateOnboardingState(completed: Boolean) {
        context.onboardingDataStore.edit { preferences ->
            // Change the state of onboarding
            preferences[onBoardingCompletedKey] = completed
        }
    }
}