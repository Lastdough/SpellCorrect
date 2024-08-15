package com.abdurraahm.spellcorrect.ui.navigation

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.abdurraahm.spellcorrect.ui.component.DefaultLinearProgressIndicator
import com.abdurraahm.spellcorrect.ui.theme.SpellCorrectTheme

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ProgressTopBar(
    modifier: Modifier = Modifier,
    progress: () -> Float,
    onCloseClick: () -> Unit
) {
    TopAppBar(
        modifier = modifier,
        title = {
            DefaultLinearProgressIndicator(
                modifier = Modifier.padding(end = 8.dp),
                progress = progress,
            )
        },
        navigationIcon = {
            IconButton(onClick = onCloseClick) {
                Icon(
                    modifier = Modifier,
                    imageVector = Icons.Rounded.Close,
                    contentDescription = ""
                )

            }
        }
    )
}


@Preview
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun ProgressTopBarPreview() {
    SpellCorrectTheme {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            ProgressTopBar(progress = { 0.6f }, onCloseClick ={} )
        }
    }
}