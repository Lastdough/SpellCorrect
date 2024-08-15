package com.abdurraahm.spellcorrect.ui.screen.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.abdurraahm.spellcorrect.data.local.model.WordEntry
import com.abdurraahm.spellcorrect.data.preview.PreviewDataSource
import com.abdurraahm.spellcorrect.ui.component.WordCard
import com.abdurraahm.spellcorrect.ui.theme.SpellCorrectTheme

@Composable
fun WordOfTheDay(
    modifier: Modifier = Modifier,
    word: WordEntry,
    onWordOfTheDayClicked: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Word of The Day",
            style = MaterialTheme.typography.headlineLarge
        )
        WordCard(
            onWordOfTheDayClicked = onWordOfTheDayClicked,
            word = word
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun WordOfTheDayPreview() {
    SpellCorrectTheme {
        WordOfTheDay(word = PreviewDataSource.singleWord()) {

        }
    }
}