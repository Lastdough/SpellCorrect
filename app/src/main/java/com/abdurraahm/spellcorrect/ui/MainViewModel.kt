package com.abdurraahm.spellcorrect.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdurraahm.spellcorrect.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {
    val onboardingCompletedState: Flow<Boolean> = mainRepository.onboardingState
    fun onOnboardingCompleted() {
        viewModelScope.launch {
            mainRepository.updateOnboardingState(true)
        }
    }

    val isDBEmpty: Flow<Boolean> = mainRepository.isDBEmpty()
    fun initDB() {
        viewModelScope.launch(Dispatchers.IO) {
            async { mainRepository.initDB() }.await()
        }
    }

    fun startTextToSpeech() {
        mainRepository.startTextToSpeech()
    }

    fun stopTextToSpeech() {
        mainRepository.stopTextToSpeech()
    }
}