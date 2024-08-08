package com.abdurraahm.spellcorrect.ui.screen.more

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.abdurraahm.spellcorrect.ui.navigation.DefaultBottomBar
import com.abdurraahm.spellcorrect.ui.navigation.DefaultTopBar
import com.abdurraahm.spellcorrect.ui.theme.SpellCorrectTheme

@Composable
fun MoreScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    moreViewModel: MoreViewModel = hiltViewModel()
) {
    val context = LocalContext.current

    MoreContent(
        modifier = modifier,
        navController = navController
    )
}

@Composable
private fun MoreContent(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    Scaffold(modifier = modifier,
        topBar = { DefaultTopBar() },
        bottomBar = { DefaultBottomBar(navController = navController) }
    ) {
        // Content
        Column(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "About SpellCorrect",
                style = TextStyle(
                    fontSize = 14.sp
                ),
            )
            Text(
                text = "SpellCorrect is a comprehensive language learning application designed to help you improve your English vocabulary and spelling skills. Whether you are a beginner or looking to polish your advanced vocabulary, SpellCorrect offers a structured approach to mastering new words and phrases.",
                style = TextStyle(
                    fontSize = 12.sp
                ),
            )
            Text(
                text = "Features",
                style = TextStyle(
                    fontSize = 14.sp
                ),
            )
            Text(
                text = "Interactive Learning With Digital Flash Card:\nAudio-Spelling Challenges:\nProgress Tracking:\nWord of The Day:",
                style = TextStyle(
                    fontSize = 12.sp
                ),
                modifier = Modifier
                    .fillMaxWidth()
            )
            Text(
                text = "Developer Connect ",
                style = TextStyle(
                    fontSize = 14.sp
                ),
            )
            Text(
                text = "Email: abdurraahm@gmail.com\ngithub: github/lasdough\nlinkdeln: link/abdurrahman-sembiring",
                style = TextStyle(
                    fontSize = 12.sp
                ),
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun HomeContentPreview() {
    SpellCorrectTheme {
        MoreContent(navController = rememberNavController())
    }
}