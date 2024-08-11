package com.abdurraahm.spellcorrect.ui.screen.home.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.abdurraahm.spellcorrect.data.local.model.WordEntry
import com.abdurraahm.spellcorrect.data.preview.PreviewDataSource
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
        Card(
            shape = RoundedCornerShape(5.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ),
            modifier = Modifier
                .fillMaxWidth(),
            onClick = onWordOfTheDayClicked,
            border = BorderStroke(2.dp, Color.Black),
            content = {
                Column(
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.Normal
                            ),
                            text = word.wordFirstLetterCapitalized
                        )
                        Text(
                            style = MaterialTheme.typography.labelLarge,
                            text = word.typeAbbreviation
                        )
                    }
                }
            }
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