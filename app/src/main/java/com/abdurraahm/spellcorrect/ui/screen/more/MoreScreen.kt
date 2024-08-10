package com.abdurraahm.spellcorrect.ui.screen.more

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.abdurraahm.spellcorrect.R
import com.abdurraahm.spellcorrect.ui.navigation.DefaultBottomBar
import com.abdurraahm.spellcorrect.ui.navigation.DefaultTopBar
import com.abdurraahm.spellcorrect.ui.theme.SpellCorrectTheme
import com.abdurraahm.spellcorrect.ui.utils.imageVectorResource

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
        topBar = { DefaultTopBar(showIcon = false) },
        bottomBar = { DefaultBottomBar(navController = navController) }
    ) {
        // Content
        Column(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Icon(
                modifier = Modifier
                    .padding(bottom = 24.dp)
                    .width(200.dp)
                    .height(50.dp)
                    .align(Alignment.CenterHorizontally),
                imageVector = R.drawable.logo_with_text.imageVectorResource(),
                contentDescription = "Logo"
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = "About SpellCorrect",
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = "SpellCorrect is a comprehensive language learning application designed to help you improve your English vocabulary and spelling skills. Whether you are a beginner or looking to polish your advanced vocabulary, SpellCorrect offers a structured approach to mastering new words and phrases.",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Justify
                )
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = "Features",
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = "Interactive Learning With Digital Flash Card:\n" +
                            "1. Audio-Spelling Challenges\n" +
                            "2. Progress Tracking\n" +
                            "3. Word of The Day",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = "Developer Connect ",
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = "GitHub: github.com/Lastdough\n" +
                            "LinkedIn: linkedin.com/in/abdurrahman-sembiring/",
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun MoreContentPreview() {
    SpellCorrectTheme {
        MoreContent(navController = rememberNavController())
    }
}