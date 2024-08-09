package com.abdurraahm.spellcorrect.data.local.source

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ProgressDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {

}