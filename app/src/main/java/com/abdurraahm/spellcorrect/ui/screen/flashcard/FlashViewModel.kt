package com.abdurraahm.spellcorrect.ui.screen.flashcard

import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdurraahm.spellcorrect.data.local.model.Section
import com.abdurraahm.spellcorrect.data.local.model.WordEntry
import com.abdurraahm.spellcorrect.data.repository.MainRepository
import com.abdurraahm.spellcorrect.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FlashViewModel @Inject constructor(
    private val mainRepository: MainRepository,
) : ViewModel() {
    fun setRate(newRate: Float) = mainRepository.updateRate(newRate)
    fun speak(text: String) = viewModelScope.launch(Dispatchers.IO) {
        mainRepository.speak(text)
    }

    private val _shuffledWords: MutableStateFlow<UiState<List<WordEntry>>> =
        MutableStateFlow(UiState.Loading)
    val shuffledWords
        get() = _shuffledWords.asStateFlow()

    private val _lastIndex = mutableIntStateOf(0)
    val lastIndex
        get() = _lastIndex

    private val _seenWords: MutableSet<Int> = mutableSetOf() // Set to track seen word indices

    fun init(section: Section) {
        viewModelScope.launch(Dispatchers.IO) {
            val initialIndex = mainRepository.getLastIndexed(section).first()
            val shownWord: Set<Int> =
                mainRepository.getSectionDataById(section.ordinal).first().shownWord
            _seenWords.addAll(shownWord)
            _lastIndex.intValue = initialIndex
        }
    }

    fun startExercise(section: Section) {
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.exerciseStart(section = section).collect { list ->
                _shuffledWords.value = UiState.Success(list)
            }
        }
    }

    fun resumeExercise(section: Section) {
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.exerciseResume(section = section).collect { list ->
                _shuffledWords.value = UiState.Success(list)
            }
        }
    }

    fun nextLastIndexed(section: Section) {
        viewModelScope.launch(Dispatchers.IO) {
            val listSize = mainRepository.getSectionListSize(section).first()
            mainRepository.getLastIndexed(section).first().let { currentIndex ->
                val nextIndex = (currentIndex + 1).coerceAtMost(listSize - 1) // Bound check
                mainRepository.saveLastIndexed(index = nextIndex, section)
                _lastIndex.intValue = nextIndex
                if (nextIndex !in _seenWords) { // Check if the word is new
                    _seenWords.add(nextIndex) // Mark the word as seen
                }
            }
        }
    }

    fun previousLastIndexed(section: Section) {
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.getLastIndexed(section).first().let { currentIndex ->
                val prevIndex = (currentIndex - 1).coerceAtLeast(0) // Boundcheck
                mainRepository.saveLastIndexed(index = prevIndex, section)
                _lastIndex.intValue = prevIndex
            }
        }
    }

    fun endExercise(section: Section) {
        viewModelScope.launch(Dispatchers.IO) {
            val listSize = mainRepository.getSectionListSize(section).first().toFloat()
            val shownWordSize = _seenWords.size.toFloat()
            val currentProgress = (shownWordSize / listSize)
            val currentShownWords = _seenWords.toSet()
            saveProgress(section.ordinal, currentProgress, currentShownWords)
            mainRepository.getLastIndexed(section).collect { i ->
                mainRepository.exerciseEnd(section, i)
            }
        }
    }

    private fun saveProgress(sectionId: Int, newProgress: Float, newShownWordSet: Set<Int>) {
        viewModelScope.launch(Dispatchers.IO) {
            mainRepository.updateSectionDataId(
                sectionId = sectionId,
                newProgress = newProgress,
                newShownWordSet = newShownWordSet
            )
            _seenWords.clear()
        }
    }


    fun shownWordSize(section: Section): StateFlow<UiState<Int>> =
        mainRepository.getSectionDataById(section.ordinal)
            .map {
                Log.d(
                    "Flash ViewModel",
                    "shownWordSize: ${it.shownWordSize} shownWord: ${it.shownWord}"
                )
                return@map UiState.Success(it.shownWordSize)
            }
            .flowOn(Dispatchers.IO)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = UiState.Loading
            )
}


