package com.abdurraahm.spellcorrect.data.local.model

data class SectionData(
    val part: Int,
    val description: String,
    val progress: Float,
) {
    val finished: Boolean
        get() = progress == 1F
    val started: Boolean
        get() = progress > 0F
}