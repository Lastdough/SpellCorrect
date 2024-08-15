package com.abdurraahm.spellcorrect.ui.screen.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.abdurraahm.spellcorrect.ui.theme.SpellCorrectTheme
import com.abdurraahm.spellcorrect.R.drawable as Drawable
import com.abdurraahm.spellcorrect.ui.utils.imageVectorResource

@Composable
fun TopContent(
    modifier: Modifier = Modifier,
    sectionFinished: Int,
    totalSection: Int
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Vocabulary", style = MaterialTheme.typography.titleLarge
            )
            Icon(
                modifier = Modifier.size(28.dp),
                imageVector = Drawable.outline_dictionary_28.imageVectorResource(),
                contentDescription = null
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$sectionFinished/$totalSection Sections",
                style = MaterialTheme.typography.titleMedium
            )
            Icon(
                modifier = Modifier.size(14.dp),
                imageVector = Icons.Default.CheckCircleOutline,
                contentDescription = null,
                tint = Color(0xFF73BBA3)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TopContentPreview() {
    SpellCorrectTheme {
        TopContent(sectionFinished = 0, totalSection = 3)
    }
}