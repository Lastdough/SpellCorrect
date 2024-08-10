package com.abdurraahm.spellcorrect.ui.screen.flashcard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdurraahm.spellcorrect.data.local.model.Section
import com.abdurraahm.spellcorrect.data.local.model.WordEntry
import com.abdurraahm.spellcorrect.data.repository.MainRepository
import com.abdurraahm.spellcorrect.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FlashViewModel @Inject constructor(
    private val mainRepository: MainRepository,
) : ViewModel() {

    private val _shuffledWords: MutableStateFlow<UiState<List<WordEntry>>> =
        MutableStateFlow(UiState.Loading)
    val shuffledWords
        get() = _shuffledWords.asStateFlow()

    fun startExercise(section: Section) {
        viewModelScope.launch {
            mainRepository.exerciseStart(section = section).collect { list ->
                _shuffledWords.value = UiState.Success(list)
            }
        }
    }

    fun resumeExercise(section: Section) {
        viewModelScope.launch {
            mainRepository.exerciseResume(section = section).collect { list ->
                _shuffledWords.value = UiState.Success(list)
            }
        }
    }
}
