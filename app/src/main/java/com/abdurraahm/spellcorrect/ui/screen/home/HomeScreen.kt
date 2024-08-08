package com.abdurraahm.spellcorrect.ui.screen.home

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.abdurraahm.spellcorrect.data.local.model.SectionData
import com.abdurraahm.spellcorrect.data.local.model.WordEntry
import com.abdurraahm.spellcorrect.data.local.source.PreviewDataSource
import com.abdurraahm.spellcorrect.ui.component.SectionCard
import com.abdurraahm.spellcorrect.ui.navigation.DefaultBottomBar
import com.abdurraahm.spellcorrect.ui.navigation.DefaultTopBar
import com.abdurraahm.spellcorrect.ui.theme.SpellCorrectTheme
import com.abdurraahm.spellcorrect.ui.utils.capitalizeFirstLetter
import com.abdurraahm.spellcorrect.ui.utils.imageVectorResource
import com.abdurraahm.spellcorrect.R.drawable as Drawable

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val wordOfTheDay = homeViewModel.wordOfTheDay
    val listOfSection = homeViewModel.listOfSection

    HomeContent(
        modifier = modifier,
        wordOfTheDay = wordOfTheDay,
        listOfSection = listOfSection,
        onWordOfTheDayClicked = {
            Toast.makeText(context, wordOfTheDay.fullDescription, Toast.LENGTH_SHORT).show()
            homeViewModel.speak(wordOfTheDay.fullDescription)
        },
        onSectionClicked = {
            Toast.makeText(context, "Section $it", Toast.LENGTH_SHORT).show()
        },
        navController = navController
    )
}

@Composable
private fun HomeContent(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    wordOfTheDay: WordEntry,
    listOfSection: List<SectionData>,
    onWordOfTheDayClicked: () -> Unit,
    onSectionClicked: (Int) -> Unit
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
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                TopContent(
                    modifier = Modifier.padding(bottom = 16.dp),
                    sectionFinished = listOfSection.count { section -> section.finished },
                    totalSection = listOfSection.size
                )
                WordOfTheDay(
                    modifier = Modifier.padding(bottom = 16.dp),
                    word = wordOfTheDay,
                    onWordOfTheDayClicked = onWordOfTheDayClicked
                )
            }
            items(listOfSection, key = { section -> section.part }) { section ->
                SectionCard(
                    section = section,
                    onSectionClicked = {
                        onSectionClicked(section.part)
                    }
                )
            }
        }
    }
}


@Composable
private fun WordOfTheDay(
    modifier: Modifier = Modifier,
    word: WordEntry,
    onWordOfTheDayClicked: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Word of The Day",
            style = MaterialTheme.typography.headlineLarge
        )
        Card(
            shape = RoundedCornerShape(5.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ),
            modifier = Modifier
                .fillMaxWidth(),
            onClick = onWordOfTheDayClicked,
            border = BorderStroke(2.dp, Color.Black),
            content = {
                Column(
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.Normal
                            ),
                            text = word.word.capitalizeFirstLetter()
                        )
                        Text(
                            style = MaterialTheme.typography.labelLarge,
                            text = word.typeAbbreviation
                        )
                    }
                }
            })
    }
}

@Composable
private fun TopContent(
    modifier: Modifier = Modifier,
    sectionFinished: Int,
    totalSection: Int
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Vocabulary", style = MaterialTheme.typography.titleLarge
            )
            Icon(
                modifier = Modifier.size(28.dp),
                imageVector = Drawable.outline_dictionary_28.imageVectorResource(),
                contentDescription = null
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$sectionFinished/$totalSection Sections",
                style = MaterialTheme.typography.titleMedium
            )
            Icon(
                modifier = Modifier.size(14.dp),
                imageVector = Icons.Default.CheckCircleOutline,
                contentDescription = null,
                tint = Color(0xFF73BBA3)
            )
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun HomeContentPreview() {
    SpellCorrectTheme {
        HomeContent(
            wordOfTheDay = PreviewDataSource.singleWord(),
            listOfSection = PreviewDataSource.section().take(3),
            onSectionClicked = {},
            onWordOfTheDayClicked = {},
            navController = rememberNavController()
        )
    }
}