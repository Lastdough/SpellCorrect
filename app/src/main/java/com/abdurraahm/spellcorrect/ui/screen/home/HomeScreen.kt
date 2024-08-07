package com.abdurraahm.spellcorrect.ui.screen.home

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.abdurraahm.spellcorrect.data.local.model.SectionData
import com.abdurraahm.spellcorrect.data.local.model.WordEntry
import com.abdurraahm.spellcorrect.data.local.source.DummyWordEntrySource
import com.abdurraahm.spellcorrect.ui.theme.SpellCorrectTheme
import com.abdurraahm.spellcorrect.ui.utils.capitalizeFirstLetter
import com.abdurraahm.spellcorrect.ui.utils.imageVectorResource
import com.abdurraahm.spellcorrect.ui.utils.toPercent
import com.abdurraahm.spellcorrect.ui.utils.toRomanNumeral
import kotlin.Int
import kotlin.OptIn
import kotlin.Unit
import com.abdurraahm.spellcorrect.R.drawable as Drawable
import com.abdurraahm.spellcorrect.R.string as String

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val wordOfTheDay = homeViewModel.wordOfTheDay
    val listOfSection = homeViewModel.listOfSection

    HomeContent(
        modifier = modifier,
        wordOfTheDay = wordOfTheDay,
        listOfSection = listOfSection,
        onSectionClicked = {
            Toast.makeText(context, "Section $it", Toast.LENGTH_SHORT).show()
        },
        onWordOfTheDayClicked = {
            Toast.makeText(context, "$wordOfTheDay", Toast.LENGTH_SHORT).show()
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeContent(
    modifier: Modifier = Modifier,
    wordOfTheDay: WordEntry,
    listOfSection: List<SectionData>,
    onSectionClicked: (Int) -> Unit,
    onWordOfTheDayClicked: () -> Unit
) {
    Scaffold(modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(title = {
                Icon(
                    modifier = Modifier.height(24.dp),
                    imageVector = Drawable.logo_with_text.imageVectorResource(),
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            })
        }) {
        // Content
        Column(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
//            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
//                sectionList.forEach { section ->
//                    SectionCard(
//                        section = section,
//                        onSectionClicked = {
//                        }
//                    )
//                }
//
//            }
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
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
}


@Composable
private fun SectionCard(
    section: SectionData,
    onSectionClicked: (Int) -> Unit
) {
    Card(
        shape = RoundedCornerShape(5.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        ),
        modifier = Modifier
            .fillMaxWidth(),
        onClick = { onSectionClicked(section.part) },
        border = BorderStroke(2.dp, Color.Black),
        content = {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    modifier = Modifier,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    Text(
                        style = MaterialTheme.typography.headlineLarge,
                        text = "Section ${section.part.toRomanNumeral()}"
                    )
                }
                Text(
                    style = MaterialTheme.typography.displaySmall,
                    text = section.description
                )

                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    progress = { section.progress },
                )
                Text(
                    modifier = Modifier.align(Alignment.End),
                    style = MaterialTheme.typography.displaySmall.copy(
                        fontSize = 12.sp
                    ),
                    text = if (!section.finished) "${section.progress.toPercent()}% Word" else "Completed"
                )

            }
        })
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
@Composable
private fun HomeContentPreview() {
    SpellCorrectTheme {
        val listOfSection = listOf(
            SectionData(
                part = 1,
                description = stringResource(id = String.desc_temp),
                progress = 0.4f
            ),
            SectionData(
                part = 2,
                description = stringResource(id = String.desc_temp),
                progress = 0.1f
            ),
            SectionData(
                part = 3,
                description = stringResource(id = String.desc_temp),
                progress = 0.83f
            ),
//            SectionCard(
//                part = 4,
//                description = stringResource(id = String.desc_temp),
//                progress = 1f
//            ),
//            SectionCard(
//                part = 5,
//                description = stringResource(id = String.desc_temp),
//                progress = 1f
//            )
        )
        val sectionFinished = listOfSection.count { it.finished }

        HomeContent(
            wordOfTheDay = DummyWordEntrySource.singleWord(),
            listOfSection = listOfSection,
            onSectionClicked = {},
            onWordOfTheDayClicked = {}
        )
    }
}

@Preview
@Composable
private fun HomeContentDarkPreview() {
    SpellCorrectTheme(darkTheme = true) {
        val listOfSection = listOf(
            SectionData(
                part = 1,
                description = stringResource(id = String.desc_temp),
                progress = 0.3f
            ),
            SectionData(
                part = 2,
                description = stringResource(id = String.desc_temp),
                progress = 0.53f
            ),
            SectionData(
                part = 3,
                description = stringResource(id = String.desc_temp),
                progress = 0.33f
            )
        )
        val sectionFinished = listOfSection.count { it.finished }


        HomeContent(
            wordOfTheDay = DummyWordEntrySource.singleWord(),
            listOfSection = listOfSection,
            onSectionClicked = {},
            onWordOfTheDayClicked = {}

        )
    }
}