package com.abdurraahm.spellcorrect.ui.state

data class SpeechToTextState(
    val isSpeaking: Boolean = false,
    val spokenText: String = "",
    val error: String? = null
)