package com.abdurraahm.spellcorrect.ui.component

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.abdurraahm.spellcorrect.ui.navigation.ProgressTopBar
import com.abdurraahm.spellcorrect.ui.theme.SpellCorrectTheme

@Composable
fun DefaultLinearProgressIndicator(
    modifier: Modifier = Modifier,
    progress: () -> Float
) {
    LinearProgressIndicator(
        modifier = modifier
            .fillMaxWidth(),
        progress = progress,
        strokeCap = StrokeCap.Round,
        color = MaterialTheme.colorScheme.onSurface,
//        trackColor = MaterialTheme.colorScheme.secondaryContainer
    )
}

@Preview
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun ProgressTopBarPreview() {
    SpellCorrectTheme {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            ProgressTopBar(progress = { 0.6f }, onCloseClick = {})
        }
    }
}
