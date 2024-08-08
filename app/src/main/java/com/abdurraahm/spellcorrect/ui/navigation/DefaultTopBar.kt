package com.abdurraahm.spellcorrect.ui.navigation

import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.abdurraahm.spellcorrect.R
import com.abdurraahm.spellcorrect.ui.theme.SpellCorrectTheme
import com.abdurraahm.spellcorrect.ui.utils.imageVectorResource

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DefaultTopBar(
    modifier: Modifier = Modifier,
    showIcon: Boolean = true
) {
    CenterAlignedTopAppBar(title = {
        if (showIcon) {
            Icon(
                modifier = modifier.height(28.dp),
                imageVector = R.drawable.logo_with_text.imageVectorResource(),
                contentDescription = "Logo",
                tint = MaterialTheme.colorScheme.onSurface
            )
        } // No need for an else block, as doing nothing is the implicit behavior
    })
}


@Preview
@Preview(uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun DefaultTopBarPreview() {
    SpellCorrectTheme {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            DefaultTopBar()
            DefaultTopBar(showIcon = false)
        }
    }
}