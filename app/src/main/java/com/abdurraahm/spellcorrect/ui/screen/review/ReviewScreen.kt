package com.abdurraahm.spellcorrect.ui.screen.review

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.abdurraahm.spellcorrect.data.local.model.SectionData
import com.abdurraahm.spellcorrect.data.preview.PreviewDataSource
import com.abdurraahm.spellcorrect.ui.component.SectionCard
import com.abdurraahm.spellcorrect.ui.component.SectionCardType
import com.abdurraahm.spellcorrect.ui.navigation.DefaultBottomBar
import com.abdurraahm.spellcorrect.ui.navigation.DefaultTopBar
import com.abdurraahm.spellcorrect.ui.theme.SpellCorrectTheme
import com.abdurraahm.spellcorrect.ui.utils.imageVectorResource
import com.abdurraahm.spellcorrect.R.drawable as Drawable


@Composable
fun ReviewScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    reviewViewModel: ReviewViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val listOfSection = reviewViewModel.listOfSection

    ReviewContent(
        modifier = modifier,
        navController = navController,
        listOfSection = listOfSection,
        onSectionCartClicked = { part, isStarted ->

        }
    )
}

@Composable
private fun ReviewContent(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    listOfSection: List<SectionData>,
    onSectionCartClicked: (Int, Boolean) -> Unit
) {
    Scaffold(modifier = modifier,
        topBar = { DefaultTopBar() },
        bottomBar = { DefaultBottomBar(navController = navController) }
    ) {
        LazyColumn(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.Start
        ) {
            item {
                Text(
                    modifier = Modifier.padding(bottom = 8.dp, top = 16.dp),
                    text = "Review", style = MaterialTheme.typography.titleLarge
                )
            }
            items(listOfSection) { section ->
                SectionCard(
                    section = section,
                    onSectionClicked = {
                        onSectionCartClicked(section.part, section.started)
                    },
                    sectionCardType = SectionCardType.TITLE_ONLY,
                    icon = if (section.started) null else Icons.Outlined.Lock
                )
            }
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    Image(
                        imageVector = Drawable.undraw_quiz_re_aol4.imageVectorResource(),
                        contentDescription = ""
                    )
                }
            }
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ReviewContentPreview() {
    SpellCorrectTheme {
        ReviewContent(
            navController = rememberNavController(),
            listOfSection = PreviewDataSource.section().take(3),
            onSectionCartClicked = { _, _ ->

            }
        )
    }
}

