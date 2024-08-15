package com.abdurraahm.spellcorrect.data.preview

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.abdurraahm.spellcorrect.R
import com.abdurraahm.spellcorrect.data.local.model.Section
import com.abdurraahm.spellcorrect.data.local.model.SectionData
import com.abdurraahm.spellcorrect.data.local.model.WordEntry

object PreviewDataSource {
    fun singleWord(): WordEntry =
        WordEntry(
            id = 0,
            word = "accurate",
            definition = listOf(
                "free from mistakes or errors",
                "able to produce results that are correct : not making mistakes"
            ),
            type = "adjective",
            ipa = "\u02c8\u00e6kj\u0259r\u0259t",
            section = Section.FIRST
        )

    @Composable
    fun section(): List<SectionData> {
        return listOf(
            SectionData(
                id = 0,
                description = stringResource(id = R.string.desc_temp),
                progress = 1f
            ),
            SectionData(
                id = 1,
                description = stringResource(id = R.string.desc_temp),
                progress = 0.83f
            ),
            SectionData(
                id = 2,
                description = stringResource(id = R.string.desc_temp),
                progress = 0f
            ),
            SectionData(
                id = 3,
                description = stringResource(id = R.string.desc_temp),
                progress = 1f
            ),
            SectionData(
                id = 4,
                description = stringResource(id = R.string.desc_temp),
                progress = 1f
            )
        )
    }

}


