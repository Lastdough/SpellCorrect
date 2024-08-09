package com.abdurraahm.spellcorrect.ui.component

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.abdurraahm.spellcorrect.data.local.model.SectionData
import com.abdurraahm.spellcorrect.data.local.source.PreviewDataSource
import com.abdurraahm.spellcorrect.ui.theme.SpellCorrectTheme

enum class SectionCardType {
    FULL,
    TITLE_ONLY
}

@Composable
fun SectionCard(
    modifier: Modifier = Modifier,
    columnModifier: Modifier = Modifier
        .padding(16.dp)
        .fillMaxWidth(),
    section: SectionData,
    sectionCardType: SectionCardType = SectionCardType.FULL,
    shape: RoundedCornerShape = RoundedCornerShape(5.dp),
    border: BorderStroke = BorderStroke(2.dp, Color.Black),
    onSectionClicked: (Int) -> Unit,
    icon: ImageVector? = null,
) {
    Card(
        shape = shape,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        modifier = modifier.fillMaxWidth(),
        onClick = { onSectionClicked(section.part) },
        border = border,
        content = {
            Column(
                modifier = columnModifier,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        style = MaterialTheme.typography.headlineLarge,
                        text = "Section ${section.partInRomanNumeral}"
                    )
                    icon?.let {
                        Icon(
                            imageVector = it,
                            contentDescription = "Locked"
                        )
                    }
                }
                when (sectionCardType) {
                    SectionCardType.FULL -> {
                        Text(
                            style = MaterialTheme.typography.displaySmall,
                            text = section.description
                        )
                        LinearProgressIndicator(
                            modifier = Modifier.fillMaxWidth(),
                            progress = { section.progress },
                        )
                        Text(
                            modifier = Modifier.align(Alignment.End),
                            style = MaterialTheme.typography.displaySmall.copy(
                                fontSize = 12.sp
                            ),
                            text = if (!section.finished) "${section.progressInPercent}% Word" else "Completed"
                        )
                    }

                    SectionCardType.TITLE_ONLY -> {}
                }


            }
        })
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun SectionCardPreview() {
    SpellCorrectTheme {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            SectionCard(
                section = PreviewDataSource.section()[0],
                onSectionClicked = {},
                icon = null
            )
            SectionCard(
                section = PreviewDataSource.section()[0],
                onSectionClicked = {},
                sectionCardType = SectionCardType.TITLE_ONLY,
                icon = Icons.Outlined.Lock
            )
        }
    }
}