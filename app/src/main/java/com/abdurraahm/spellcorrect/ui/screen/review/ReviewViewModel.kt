package com.abdurraahm.spellcorrect.ui.screen.review

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdurraahm.spellcorrect.data.local.model.SectionData
import com.abdurraahm.spellcorrect.data.repository.MainRepository
import com.abdurraahm.spellcorrect.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
}