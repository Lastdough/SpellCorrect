package com.abdurraahm.spellcorrect.ui.screen.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdurraahm.spellcorrect.data.local.model.Section
import com.abdurraahm.spellcorrect.data.local.model.SectionData
import com.abdurraahm.spellcorrect.data.local.model.WordEntry
import com.abdurraahm.spellcorrect.data.repository.MainRepository
import com.abdurraahm.spellcorrect.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val mainRepository: MainRepository,
) : ViewModel() {
    private val _listOfSection: MutableStateFlow<UiState<List<SectionData>>> =
        MutableStateFlow(UiState.Loading)
    val listOfSection
        get() = _listOfSection.asStateFlow()

    fun getListOfSection() {
        viewModelScope.launch {
            mainRepository.sectionInDB().collect { data ->
                _listOfSection.value = UiState.Success(data)
            }
        }
    }

    private val _listOf10RandomWords: MutableStateFlow<UiState<List<WordEntry>>> =
        MutableStateFlow(UiState.Loading)
    val listOf10RandomWords
        get() = _listOf10RandomWords.asStateFlow()

    fun get10WordsOnSection(section: Section) {
        viewModelScope.launch(Dispatchers.IO) {
            val list = mainRepository.reviewSpeak(section).first()
            _listOf10RandomWords.value = UiState.Success(list)
        }
    }

    // Speaking
    fun speech() = mainRepository.speechToTextManager()

    // TTS
    fun speak(text: String) = viewModelScope.launch(Dispatchers.IO) {
        mainRepository.speak(text)
    }
}