package com.abdurraahm.spellcorrect.ui.screen.review

import android.content.Context
import androidx.lifecycle.ViewModel
import com.abdurraahm.spellcorrect.R
import com.abdurraahm.spellcorrect.data.local.model.SectionData
import com.abdurraahm.spellcorrect.data.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

@HiltViewModel
class ReviewViewModel @Inject constructor(
    private val mainRepository: MainRepository,
    @ApplicationContext private val context: Context
) : ViewModel() {
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
            progress = 0f
        )
    )
}