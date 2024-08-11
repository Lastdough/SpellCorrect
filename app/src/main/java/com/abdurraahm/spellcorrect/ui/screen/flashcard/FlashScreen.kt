package com.abdurraahm.spellcorrect.ui.screen.flashcard

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.VolumeUp
import androidx.compose.material.icons.outlined.SkipNext
import androidx.compose.material.icons.outlined.SkipPrevious
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.abdurraahm.spellcorrect.data.local.model.Exercise
import com.abdurraahm.spellcorrect.data.local.model.Section
import com.abdurraahm.spellcorrect.data.local.model.WordEntry
import com.abdurraahm.spellcorrect.data.preview.PreviewDataSource
import com.abdurraahm.spellcorrect.ui.screen.loading.CircularLoading
import com.abdurraahm.spellcorrect.ui.state.UiState
import com.abdurraahm.spellcorrect.ui.theme.SpellCorrectTheme
import com.wajahatkarim.flippable.Flippable
import com.wajahatkarim.flippable.FlippableController
import com.wajahatkarim.flippable.rememberFlipController

@Composable
fun FlashScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    sectionId: Int,
    flashViewModel: FlashViewModel = hiltViewModel(),
    exerciseState: Int
) {
    val section = Section.entries[sectionId]
    val configuration = LocalConfiguration.current

    LaunchedEffect(Unit) {
        flashViewModel.init(section)
        when (Exercise.entries[exerciseState]) {
            Exercise.START -> flashViewModel.startExercise(section)
            Exercise.RESUME -> flashViewModel.resumeExercise(section)
        }
    }
    val context = LocalContext.current
    when (val shuffledWords = flashViewModel.shuffledWords.collectAsState().value) {
        is UiState.Error -> {
        }

        UiState.Loading -> {
            CircularLoading()
        }

        is UiState.Success -> {
            val list = shuffledWords.data
            val index = flashViewModel.lastIndex.intValue
            val word = list[index]
            FlashContent(
                modifier = modifier,
                word = word,
                onWordClicked = {
                    flashViewModel.speak(word.word)
                },
                onPreviousClicked = {
                    flashViewModel.previousLastIndexed(section)
                },
                onNextClicked = {
                    flashViewModel.nextLastIndexed(section)
                },
                onBackButtonClicked = {
                    navController.navigateUp()
                    flashViewModel.endExercise(section = section)
                },
                onDefinitionClicked = {
                    flashViewModel.speak(word.fullDescription)
                },
                currentIndex = index,
                lastIndex = list.lastIndex,
                i = flashViewModel.wordsShownCount.intValue,
                configuration = configuration
            )
            BackHandler {
                navController.navigateUp()
                flashViewModel.endExercise(section = section)
            }
        }
    }
}

@Composable
fun WordCard(
    modifier: Modifier = Modifier,
    columnModifier: Modifier = Modifier
        .padding(16.dp)
        .fillMaxWidth(),
    word: WordEntry,
    shape: RoundedCornerShape = RoundedCornerShape(5.dp),
    border: BorderStroke = BorderStroke(5.dp, Color(0xFF758694)),
    onWordClicked: () -> Unit,
    icon: ImageVector? = null,
    configuration: Configuration,
    onDefinitionClicked: () -> Unit,
) {
    val controller = rememberFlipController()

    val flipController = remember { FlippableController() }
    val fraction = when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            0.85f
        }

        else -> {
            0.25f
        }
    }

    val fontSize = when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            56.sp
        }

        else -> {
            32.sp
        }
    }

    val paddingHorizontal = when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            16.dp
        }

        else -> {
            0.dp
        }
    }

    Flippable(
        modifier = modifier.padding(horizontal = paddingHorizontal),
        flipOnTouch = true,
        frontSide = {
            Card(
                shape = shape,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(fraction),
                border = border,
                content = {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = word.wordFirstLetterCapitalized,
                                style = MaterialTheme.typography.headlineLarge.copy(fontSize = fontSize)
                            )
                            IconButton(onClick = onWordClicked) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Outlined.VolumeUp,
                                    contentDescription = null
                                )
                            }
                        }
                    }
                }
            )
        }, backSide = {
            Card(
                shape = shape,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                modifier = modifier
                    .fillMaxWidth()
                    .fillMaxHeight(fraction),
                border = border,
                content = {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = word.wordFirstLetterCapitalized,
                                style = MaterialTheme.typography.headlineLarge
                            )
                            Text(text = word.typeClean)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (word.hasIPA) {
                                Text(text = word.ipa)
                            }
                            IconButton(onClick = onWordClicked) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Outlined.VolumeUp,
                                    contentDescription = null
                                )
                            }
                        }
                        Text(
                            modifier = Modifier
                                .clickable {
                                    onDefinitionClicked()
                                },
                            text = word.definitionWithNumber, textAlign = TextAlign.Justify
                        )
                    }
                }
            )
        }, flipController = controller
    )
}


@Composable
private fun FlashContent(
    modifier: Modifier = Modifier,
    word: WordEntry,
    onWordClicked: () -> Unit,
    onPreviousClicked: () -> Unit,
    onNextClicked: () -> Unit,
    onBackButtonClicked: () -> Unit,
    currentIndex: Int,
    lastIndex: Int,
    onDefinitionClicked: () -> Unit,
    i: Int,
    configuration: Configuration
) {

    Scaffold(modifier = modifier) {
        // Content
        Column(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 16.dp, vertical = 32.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
//            Text(text = "${i}")
//            LinearProgressIndicator(
//                modifier = Modifier.fillMaxWidth(),
//                progress = {  },
//            )
            WordCard(
                word = word,
                onWordClicked = onWordClicked,
                configuration = configuration,
                onDefinitionClicked = onDefinitionClicked
            )
            Row(
                modifier = Modifier.fillMaxSize(),
                Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onBackButtonClicked) {
                    Icon(
                        modifier = Modifier.size(32.dp),
                        imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = null
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    IconButton(onClick = onPreviousClicked, enabled = currentIndex > 0) {
                        // Disable at first index
                        Icon(
                            modifier = Modifier.size(32.dp),
                            imageVector = Icons.Outlined.SkipPrevious,
                            contentDescription = null
                        )
                    }
                    IconButton(onClick = onNextClicked, enabled = currentIndex < lastIndex) {
                        // Disable at last index
                        Icon(
                            modifier = Modifier.size(32.dp),
                            imageVector = Icons.Outlined.SkipNext,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Preview(
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape"
)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun FlashContentPreview() {
    SpellCorrectTheme {
        val configuration = LocalConfiguration.current
        FlashContent(
            word = PreviewDataSource.singleWord(),
            onWordClicked = {},
            onPreviousClicked = {},
            onNextClicked = {},
            onBackButtonClicked = {},
            currentIndex = 0,
            lastIndex = 1,
            onDefinitionClicked = {},
            i = 1,
            configuration = configuration,
        )
    }
}