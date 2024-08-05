package com.abdurraahm.spellcorrect.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.abdurraahm.spellcorrect.ui.theme.SpellCorrectTheme

@Composable
fun CustomButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    text: String,
    @DrawableRes iconPainter: Int? = null,
    isOutlined: Boolean = true,
    contentDescriptionText: String? = null,
    useDarkTheme: Boolean = false,
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(99.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xff2f2e41)),
        modifier = modifier
            .fillMaxWidth()
            .semantics(mergeDescendants = true) {
                contentDescription = contentDescriptionText ?: text
            },
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 5.dp
        ),
        content = {
            Text(
                text = text,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .wrapContentHeight(align = Alignment.CenterVertically)
            )
            if (iconPainter != null) {
                Spacer(modifier = Modifier.padding(8.dp))
                Icon(
                    painter = painterResource(id = iconPainter),
                    contentDescription = null
                )
            }
        }
    )
}

@Preview
@Composable
private fun CustomButtonPreview() {
    SpellCorrectTheme {
        CustomButton(onClick = { /*TODO*/ }, text = "Get Started")
    }
}
