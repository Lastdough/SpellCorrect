package com.abdurraahm.spellcorrect.ui

import androidx.lifecycle.ViewModel
import com.abdurraahm.spellcorrect.data.repository.WordEntryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val wordEntryRepository: WordEntryRepository
) : ViewModel() {
    var wordOfTheDay = wordEntryRepository.wordOfTheDay()
}