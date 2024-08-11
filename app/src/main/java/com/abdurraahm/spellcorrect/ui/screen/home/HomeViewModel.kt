package com.abdurraahm.spellcorrect.ui.screen.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdurraahm.spellcorrect.data.local.model.SectionData
import com.abdurraahm.spellcorrect.data.local.model.WordEntry
import com.abdurraahm.spellcorrect.data.repository.MainRepository
import com.abdurraahm.spellcorrect.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {
    private val _wordOfTheDay: MutableStateFlow<UiState<WordEntry>> =
        MutableStateFlow(UiState.Loading)
    val wordOfTheDay
        get() = _wordOfTheDay.asStateFlow()

    fun getWordOfTheDay() {
        viewModelScope.launch(Dispatchers.IO) {
            // Trigger the flow and consume its elements using collect
            mainRepository.wordOfTheDay().collect { word ->
                _wordOfTheDay.value = UiState.Success(word)
            }
        }

    }

     fun speak(text: String) = mainRepository.speak(text)

    private val _listOfSection: MutableStateFlow<UiState<List<SectionData>>> =
        MutableStateFlow(UiState.Loading)
    val listOfSection
        get() = _listOfSection.asStateFlow()

    fun getListOfSection() {
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.sectionInDB().collect { data ->
                _listOfSection.value = UiState.Success(data)
            }
        }
    }

    // Masalah asalnya dari
    // Realita != Idealita
    // Hipotesa
    // Pembuktian Hipotesa
}