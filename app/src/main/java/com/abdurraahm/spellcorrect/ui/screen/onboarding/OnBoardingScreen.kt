package com.abdurraahm.spellcorrect.ui.screen.onboarding

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.abdurraahm.spellcorrect.R
import com.abdurraahm.spellcorrect.ui.component.CustomButton
import com.abdurraahm.spellcorrect.ui.theme.SpellCorrectTheme
import com.abdurraahm.spellcorrect.ui.utils.imagePainterResource
import com.abdurraahm.spellcorrect.ui.utils.imageVectorResource

@Composable
fun OnBoardingScreen(
    modifier: Modifier = Modifier,
    onClickGetStartedClicked: () -> Unit,
) {
    Scaffold(
        modifier = modifier,
    ) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .padding(horizontal = 32.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceAround,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector =
                R.drawable.logo_with_text.imageVectorResource(),
                contentDescription = ""
            )
            Image(
                painter = R.drawable.poetry_pana.imagePainterResource(),
                contentDescription = ""
            )
            CustomButton(onClick = onClickGetStartedClicked, text = "Get Started")
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun OnBoardingScreenPreview() {
    SpellCorrectTheme {
        OnBoardingScreen(onClickGetStartedClicked = { })
    }
}