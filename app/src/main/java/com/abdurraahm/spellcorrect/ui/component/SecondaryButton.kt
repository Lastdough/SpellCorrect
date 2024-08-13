package com.abdurraahm.spellcorrect.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.abdurraahm.spellcorrect.ui.theme.SpellCorrectTheme

@Composable
fun SecondaryButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: () -> Boolean = { true },
    contentDescriptionText: String? = null,
    roundedCornerShape: RoundedCornerShape = RoundedCornerShape(5.dp),
    content: @Composable RowScope.() -> Unit
) {
    Button(
        enabled = enabled(),
        onClick = onClick,
        shape = roundedCornerShape,
        border = ButtonDefaults.outlinedButtonBorder.copy(width = 1.dp),
//        border = BorderStroke(1.dp, Color.Black),
        colors = ButtonDefaults.buttonColors(
            contentColor = MaterialTheme.colorScheme.onSurface,
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        modifier = modifier
            .fillMaxWidth()
            .semantics(mergeDescendants = true) {
                contentDescription = contentDescriptionText ?: ""
            },
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 5.dp
        ),
        content = content
    )
}

@Preview
@Composable
private fun SecondaryButtonPreview() {
    SpellCorrectTheme {
        SecondaryButton(
            onClick = { },
            content = {
                Row(
                    modifier = Modifier,
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        tint = MaterialTheme.colorScheme.onSurface,
                        imageVector = Icons.Filled.Mic,
                        contentDescription = null
                    )
                    Text(
                        text = "Tap To Speak",
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .padding(vertical = 32.dp)
                    )
                }

            })
    }
}
