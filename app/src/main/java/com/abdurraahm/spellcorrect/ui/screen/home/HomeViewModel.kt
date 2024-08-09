package com.abdurraahm.spellcorrect.ui.screen.home

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdurraahm.spellcorrect.R
import com.abdurraahm.spellcorrect.data.local.model.SectionData
import com.abdurraahm.spellcorrect.data.local.model.WordEntry
import com.abdurraahm.spellcorrect.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val _wordOfTheDay: MutableState<WordEntry> = mutableStateOf(
        WordEntry(
            word = "Word Of The Day is in Progress",
            type = "",
            ipa = "",
            definition = listOf()
        )
    )
    val wordOfTheDay = _wordOfTheDay.value

    fun wordOfTheDayInvoke(){
        viewModelScope.launch {
            // Trigger the flow and consume its elements using collect
            mainRepository.wordOfTheDay().collect { e ->
                val wordE = e
                // Update View with the latest favorite news
                _wordOfTheDay.value = wordE
            }
        }
    }

    init {

    }

// Masalah asalnya dari
// Realita != Idealita
// Hipotesa
// Pembuktian Hipotesa

    fun setRate(newRate: Float) = mainRepository.updateRate(newRate)
    fun speak(text: String) = mainRepository.speak(text)

    val listOfSection = listOf(
        SectionData(
            part = 1,
            description = context.getString(R.string.desc_temp),
            progress = 0.03f
        ),
        SectionData(
            part = 2,
            description = context.getString(R.string.desc_temp),
            progress = 0.03f
        ),
        SectionData(
            part = 3,
            description = context.getString(R.string.desc_temp),
            progress = 0.03f
        )
    )
}