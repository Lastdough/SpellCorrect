package com.abdurraahm.spellcorrect.ui.screen.home

import android.content.Context
import androidx.lifecycle.ViewModel
import com.abdurraahm.spellcorrect.R
import com.abdurraahm.spellcorrect.data.local.model.SectionData
import com.abdurraahm.spellcorrect.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {
    val wordOfTheDay = mainRepository.wordOfTheDay()

    fun setRate(newRate: Float) = mainRepository.updateRate(newRate)
    fun speak(text: String) = mainRepository.speak(text)

    val listOfSection = listOf(
        SectionData(
            part = 1,
            description = context.getString(R.string.desc_temp),
            progress = 1f
        ),
        SectionData(
            part = 2,
            description = context.getString(R.string.desc_temp),
            progress = 0.5f
        ),
        SectionData(
            part = 3,
            description = context.getString(R.string.desc_temp),
            progress = 0f
        )
    )
}