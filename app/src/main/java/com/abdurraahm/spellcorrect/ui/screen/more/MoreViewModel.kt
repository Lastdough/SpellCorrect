package com.abdurraahm.spellcorrect.ui.screen.more

import androidx.lifecycle.ViewModel
import com.abdurraahm.spellcorrect.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MoreViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    ) : ViewModel() {
}