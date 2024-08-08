package com.abdurraahm.spellcorrect.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdurraahm.spellcorrect.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {
    val onboardingCompletedState = mainRepository.onboardingState
    fun onOnboardingCompleted() {
        viewModelScope.launch {
            mainRepository.updateOnboardingState(true)
        }
    }

    fun startTextToSpeech() {
        mainRepository.startTextToSpeech()
    }

    fun stopTextToSpeech() {
        mainRepository.stopTextToSpeech()
    }
}