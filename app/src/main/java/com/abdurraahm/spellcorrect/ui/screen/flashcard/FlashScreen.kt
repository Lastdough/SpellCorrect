package com.abdurraahm.spellcorrect.ui.screen.flashcard

import android.content.res.Configuration
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.abdurraahm.spellcorrect.data.local.model.Mode
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
        when (Mode.entries[exerciseState]) {
            Mode.START -> flashViewModel.startExercise(section)
            Mode.RESUME -> flashViewModel.resumeExercise(section)
            Mode.WORD -> flashViewModel.displayWordOfTheDay()
        }
    }

    val shownWordSize by flashViewModel.shownWordSize(section).collectAsState()
    when (val shuffledWords = flashViewModel.shuffledWords.collectAsState().value) {
        is UiState.Error -> {}
        UiState.Loading -> CircularLoading()
        is UiState.Success -> {
            ShuffleWordsFlash(
                shuffledWords = shuffledWords,
                flashViewModel = flashViewModel,
                modifier = modifier,
                section = section,
                navController = navController,
                shownWordSize = shownWordSize,
                configuration = configuration
            )
        }
        UiState.Empty -> {
            WordOfTheDayFlash(
                modifier = modifier,
                navController = navController,
                shownWordSize = shownWordSize,
                configuration = configuration,
                wordOfTheDayState = flashViewModel.wordOfTheDay.collectAsState().value,
                onSpeakWord = flashViewModel::speak
            )
        }
    }
}

@Composable
private fun ShuffleWordsFlash(
    shuffledWords: UiState.Success<List<WordEntry>>,
    flashViewModel: FlashViewModel,
    modifier: Modifier = Modifier,
    section: Section,
    navController: NavHostController,
    shownWordSize: UiState<Int>,
    configuration: Configuration
) {
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
        shownWordState = shownWordSize,
        configuration = configuration,
        isAList = true
    )
    BackHandler {
        navController.navigateUp()
        flashViewModel.endExercise(section = section)
        Log.d("Flash Screen", "FlashScreen: back in success")
    }
}

@Composable
private fun WordOfTheDayFlash(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    shownWordSize: UiState<Int>,
    configuration: Configuration,
    wordOfTheDayState: UiState<WordEntry>,
    onSpeakWord: (String) -> Unit
) {
    when (wordOfTheDayState) {
        UiState.Loading -> CircularLoading()
        is UiState.Error -> {}
        is UiState.Success -> {
            FlashContent(
                modifier = modifier,
                word = wordOfTheDayState.data,
                onWordClicked = {
                    onSpeakWord(wordOfTheDayState.data.word)
                },
                onPreviousClicked = {},
                onNextClicked = {},
                onBackButtonClicked = {
                    navController.navigateUp()
                },
                onDefinitionClicked = {
                    onSpeakWord(wordOfTheDayState.data.fullDescription)
                },
                currentIndex = 0,
                lastIndex = 0,
                shownWordState = shownWordSize,
                configuration = configuration,
                isAList = false
            )
            BackHandler {
                navController.navigateUp()
                Log.d("Flash Screen", "FlashScreen: back in empty")
            }
        }

        UiState.Empty -> {}
    }
}

fun Modifier.cardModifier(configuration: Configuration) = when (configuration.orientation) {
    Configuration.ORIENTATION_LANDSCAPE -> {
        this
            .fillMaxWidth()
            .fillMaxHeight(0.85f)
    }

    else -> {
        this
            .fillMaxWidth()
            .fillMaxHeight(0.5f)
    }
}

fun paddingHorizontal(configuration: Configuration) = when (configuration.orientation) {
    Configuration.ORIENTATION_LANDSCAPE -> {
        16.dp
    }

    else -> {
        0.dp
    }
}

@Composable
fun WordCard(
    modifier: Modifier = Modifier,
    columnModifier: Modifier = Modifier
        .padding(16.dp)
        .fillMaxSize(),
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


    val fontSize = when (configuration.orientation) {
        Configuration.ORIENTATION_LANDSCAPE -> {
            56.sp
        }

        else -> {
            32.sp
        }
    }



    Flippable(
        flipController = flipController,
        modifier = modifier.padding(horizontal = paddingHorizontal(configuration)),
        flipOnTouch = true,
        frontSide = {
            Card(
                shape = shape,
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                ),
                modifier = Modifier
                    .cardModifier(configuration),
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
                    .cardModifier(configuration),
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
                            text = word.definitionWithNumber,
                        )
                    }
                }
            )
        }
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
    isAList: Boolean,
    onDefinitionClicked: () -> Unit,
    shownWordState: UiState<Int>,
    configuration: Configuration
) {
    Scaffold(modifier = modifier) {
        // Content
        Column(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 16.dp, vertical = 32.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
        ) {
            if (isAList) {
//                when (shownWordState) {
//                    is UiState.Success -> {
//                        LinearProgressIndicator(
//                            progress = {
//                                shownWordState.data.toFloat() / lastIndex.toFloat()
//                            },
//                            modifier = Modifier
//                                .padding(
//                                    bottom = 16.dp,
//                                    start = paddingHorizontal(configuration),
//                                    end = paddingHorizontal(configuration)
//                                )
//                                .fillMaxWidth(),
//                        )
//                    }
//
//                    else -> {}
//                }
            }
            WordCard(
                word = word,
                onWordClicked = onWordClicked,
                configuration = configuration,
                onDefinitionClicked = onDefinitionClicked
            )
            Spacer(modifier = modifier.padding(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onBackButtonClicked) {
                    Icon(
                        modifier = Modifier.size(32.dp),
                        imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = null
                    )
                }
                if (isAList) {
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
}

class IsAListProvider : PreviewParameterProvider<Boolean> {
    override val values: Sequence<Boolean> = sequenceOf(true, false)
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape",
)
@Preview(
    device = "spec:width=411dp,height=891dp,dpi=420,isRound=false,chinSize=0dp,orientation=landscape",
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun FlashContentPreview(
    @PreviewParameter(IsAListProvider::class) isAList: Boolean
) {

    SpellCorrectTheme {
        val configuration = LocalConfiguration.current
        FlashContent(
            word = PreviewDataSource.singleWord(),
            onWordClicked = {},
            onPreviousClicked = {},
            onNextClicked = {},
            onBackButtonClicked = {},
            currentIndex = 1,
            lastIndex = 5,
            onDefinitionClicked = {},
            shownWordState = UiState.Success(1),
            configuration = configuration,
            isAList = isAList
        )
    }
}