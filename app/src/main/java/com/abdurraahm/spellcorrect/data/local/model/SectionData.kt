package com.abdurraahm.spellcorrect.data.local.model

data class SectionData(
    val part: Int,
    val description: String,
    val progress: Float,
) {
    val finished: Boolean = progress == 1F
}