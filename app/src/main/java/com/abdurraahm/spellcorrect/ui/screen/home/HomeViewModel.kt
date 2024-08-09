package com.abdurraahm.spellcorrect.ui.screen.home

import android.content.Context
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.abdurraahm.spellcorrect.R
import com.abdurraahm.spellcorrect.data.local.model.SectionData
import com.abdurraahm.spellcorrect.data.local.model.WordEntry
import com.abdurraahm.spellcorrect.data.repository.MainRepository
import com.abdurraahm.spellcorrect.ui.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {

//    fun getWeatherForecast(): Flow<String> {
//        return forecastRepository
//            .getWeatherForecastEveryTwoSeconds(spendingDetailsRequest)
//            .map {
//                it + " °C"
//            }
//    }

    private val _wordOfTheDay: MutableStateFlow<UiState<WordEntry>> =
        MutableStateFlow(UiState.Loading)
    val wordOfTheDay = _wordOfTheDay.asStateFlow()

    fun getWordOfTheDay() {
        viewModelScope.launch {
            // Trigger the flow and consume its elements using collect
            mainRepository.wordOfTheDay().collect { word ->
                _wordOfTheDay.value = UiState.Success(word)
            }
        }

    }


// Masalah asalnya dari
// Realita != Idealita
// Hipotesa
// Pembuktian Hipotesa

    fun setRate(newRate: Float) = mainRepository.updateRate(newRate)
    fun speak(text: String) = mainRepository.speak(text)

    val listOfSection = listOf(
        SectionData(
            part = 1,
            description = context.getString(R.string.desc_temp),
            progress = 0.03f
        ),
        SectionData(
            part = 2,
            description = context.getString(R.string.desc_temp),
            progress = 0.03f
        ),
        SectionData(
            part = 3,
            description = context.getString(R.string.desc_temp),
            progress = 0.03f
        )
    )
}