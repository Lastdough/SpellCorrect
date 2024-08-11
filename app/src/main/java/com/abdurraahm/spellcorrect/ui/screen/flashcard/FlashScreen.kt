package com.abdurraahm.spellcorrect.ui.screen.flashcard

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.VolumeUp
import androidx.compose.material.icons.outlined.SkipNext
import androidx.compose.material.icons.outlined.SkipPrevious
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.abdurraahm.spellcorrect.data.local.model.Exercise
import com.abdurraahm.spellcorrect.data.local.model.Section
import com.abdurraahm.spellcorrect.data.local.model.WordEntry
import com.abdurraahm.spellcorrect.data.preview.PreviewDataSource
import com.abdurraahm.spellcorrect.ui.screen.loading.CircularLoading
import com.abdurraahm.spellcorrect.ui.state.UiState
import com.abdurraahm.spellcorrect.ui.theme.SpellCorrectTheme

@Composable
fun FlashScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    sectionId: Int,
    flashViewModel: FlashViewModel = hiltViewModel(),
    exerciseState: Int
) {
    val section = Section.entries[sectionId]

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
                i = flashViewModel.wordsShownCount.intValue
            )
            BackHandler {
                navController.navigateUp()
                flashViewModel.endExercise(section = section)
            }
        }
    }


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
    i: Int
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
            Text(text = "${i}")
            Column() {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = word.wordFirstLetterCapitalized)
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
            }
            Text(modifier = Modifier.clickable {
                onDefinitionClicked()
            }, text = word.definitionWithNumber, textAlign = TextAlign.Justify)
            Row(
                modifier = Modifier.fillMaxSize(),
                Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onBackButtonClicked) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                        contentDescription = null
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    IconButton(onClick = onPreviousClicked, enabled = currentIndex > 0) {
                        // Disable at first index
                        Icon(
                            imageVector = Icons.Outlined.SkipPrevious,
                            contentDescription = null
                        )
                    }
                    IconButton(onClick = onNextClicked, enabled = currentIndex < lastIndex) {
                        // Disable at last index
                        Icon(
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
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun FlashContentPreview() {
    SpellCorrectTheme {
        FlashContent(
            word = PreviewDataSource.singleWord(),
            onWordClicked = {},
            onPreviousClicked = {},
            onNextClicked = {},
            onDefinitionClicked = {},
            onBackButtonClicked = {},
            currentIndex = 0,
            lastIndex = 0,
            i = 1,
        )
    }
}