package com.abdurraahm.spellcorrect.ui.navigation

import androidx.compose.foundation.layout.height
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.abdurraahm.spellcorrect.R
import com.abdurraahm.spellcorrect.ui.utils.imageVectorResource

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun DefaultTopBar(
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(title = {
        Icon(
            modifier = modifier.height(24.dp),
            imageVector = R.drawable.logo_with_text.imageVectorResource(),
            contentDescription = "Logo",
            tint = MaterialTheme.colorScheme.onSurface
        )
    })
}