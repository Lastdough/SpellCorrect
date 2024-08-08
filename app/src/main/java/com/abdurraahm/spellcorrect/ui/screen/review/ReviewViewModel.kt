package com.abdurraahm.spellcorrect.ui.screen.review

import androidx.lifecycle.ViewModel
import com.abdurraahm.spellcorrect.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    ) : ViewModel() {
}