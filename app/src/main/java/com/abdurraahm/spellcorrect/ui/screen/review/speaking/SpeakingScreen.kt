package com.abdurraahm.spellcorrect.ui.screen.review.speaking

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.abdurraahm.spellcorrect.data.local.converter.StringWordEntryConverter
import com.abdurraahm.spellcorrect.data.local.model.Section
import com.abdurraahm.spellcorrect.data.local.model.WordEntry
import com.abdurraahm.spellcorrect.data.preview.PreviewDataSource
import com.abdurraahm.spellcorrect.ui.component.SecondaryButton
import com.abdurraahm.spellcorrect.ui.component.WordCard
import com.abdurraahm.spellcorrect.ui.navigation.ProgressTopBar
import com.abdurraahm.spellcorrect.ui.navigation.Screen
import com.abdurraahm.spellcorrect.ui.screen.loading.CircularLoading
import com.abdurraahm.spellcorrect.ui.screen.review.ReviewViewModel
import com.abdurraahm.spellcorrect.ui.state.SpeechToTextState
import com.abdurraahm.spellcorrect.ui.state.UiState
import com.abdurraahm.spellcorrect.ui.theme.SpellCorrectTheme
import java.util.Timer
import java.util.TimerTask

@Composable
fun SpeakingScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    sectionId: Int,
    reviewViewModel: ReviewViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        reviewViewModel.get10WordsOnSection(Section.entries[sectionId])
    }

    val context = LocalContext.current
    val sttState by reviewViewModel.speech().state.collectAsState()
    val listOfWords: UiState<List<WordEntry>> =
        reviewViewModel.listOf10RandomWords.collectAsState().value
    var currentIndex by remember { mutableIntStateOf(0) }
    var showConfirmationDialogState by remember { mutableStateOf(false) }
    var correctAnswers by remember { mutableIntStateOf(0) }

    when (listOfWords) {
        UiState.Empty -> {}
        is UiState.Error -> {}
        UiState.Loading -> CircularLoading()
        is UiState.Success -> {
            val words = listOfWords.data
            val word = words[currentIndex]
            SpeakingContent(
                modifier = modifier,
                word = word.word,
                sectionId = sectionId,
                hasPermission = reviewViewModel.speech().hasPermission(),
                state = sttState,
                start = { lang ->
                    reviewViewModel.speech().startRecognizing(lang)
                },
                stop = {
                    reviewViewModel.speech().stopRecognizing()
                },
                reset = {
                    reviewViewModel.speech().resetState()
                },
                progress = { currentIndex.toFloat() / words.size },
                onSubmitClicked = {
                    val spokenText = sttState.spokenText.lowercase() // Normalize to lowercase
                    val currentWord = word.word.lowercase() // Normalize to lowercase

                    if (spokenText == currentWord) {
                        // Words match, handle as correct
                        correctAnswers++
                        Toast.makeText(context, "Correct!", Toast.LENGTH_SHORT).show()
                    } else {
                        // Words don't match, handle as incorrect
                        Toast.makeText(context, "Try again", Toast.LENGTH_SHORT).show()
                    }

                    if (currentIndex < words.size - 1) {
                        currentIndex++
                        Toast.makeText(context, "$currentIndex / ${words.size}", Toast.LENGTH_SHORT)
                            .show()
                        reviewViewModel.speech().resetState()
                    } else {
                        Toast.makeText(context, "End Of The Word", Toast.LENGTH_SHORT).show()
                        navController.navigate(
                            Screen.Result.createRoute(
                                correctAnswers = correctAnswers,
                                totalQuestions = words.size
                            )
                        )
                    }
                },
                showConfirmationDialog = showConfirmationDialogState,
                showConfirmationDialogState = {
                    showConfirmationDialogState = it
                },
                navController = navController
            )
        }
    }


}

@Composable
private fun SpeakingContent(
    modifier: Modifier = Modifier,
    word: String,
    state: SpeechToTextState,
    hasPermission: Boolean,
    start: (String) -> Unit,
    stop: () -> Unit,
    progress: () -> Float,
    onSubmitClicked: () -> Unit,
    showConfirmationDialogState: (Boolean) -> Unit,
    showConfirmationDialog: Boolean,
    navController: NavHostController,
    reset: () -> Unit,
    sectionId: Int
) {
    BackHandler {
        showConfirmationDialogState(true)

    }

    if (showConfirmationDialog) {
        AlertDialog(
            shape = RoundedCornerShape(5.dp),
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            onDismissRequest = { showConfirmationDialogState(false) },
            title = { Text("Confirm Exit") },
            text = { Text("Are you sure you want to exit?\nYour progress will not be saved.") },
            confirmButton = {
                SecondaryButton(onClick = {
                    showConfirmationDialogState(false)
                    stop()
                    reset()
                    navController.navigateUp()
                    // Perform actual closing logic here (e.g., navigate back)}) {
                }, content = {
                    Text(
                        "Exit",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                )
            },
            dismissButton = {
                SecondaryButton(
                    onClick = {
                        showConfirmationDialogState(false)
                    }
                ) {
                    Text(
                        "Continue",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        )
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            ProgressTopBar(
                progress = progress,
                onCloseClick = {
                    showConfirmationDialogState(true)
                }
            )
        },
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(top = 8.dp, bottom = 24.dp, start = 16.dp, end = 16.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Speak This Word...", style = MaterialTheme.typography.titleLarge
                )
                AnimatedContent(targetState = word, label = "Word Card") { w ->
                    WordCard(
                        onWordOfTheDayClicked = { },
                        word = StringWordEntryConverter.convertToEmptyWordEntry(
                            word = w,
                            sectionId = sectionId
                        ),
                        disableClick = true,
                        showType = false
                    )
                }
                AnimatedContent(targetState = state.isSpeaking, label = "") { isSpeaking ->
                    if (isSpeaking) {
                        AnimatedListeningText()
                    } else {
                        Text(
                            text = state.spokenText.ifEmpty { "" },
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }
            SpeakButton(state, onSpeakButtonClick = {
                if (hasPermission) {
                    if (!state.isSpeaking) {
                        start("en")
                    } else {
                        stop()
                    }
                }

            })
            SecondaryButton(onClick = onSubmitClicked, enabled = {
                state.spokenText.isNotEmpty()
            }) {
                Text(
                    modifier = Modifier.padding(vertical = 6.dp),
                    text = "Next",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
fun AnimatedListeningText(
    isSpeaking: Boolean = true
) {
    var dots by remember { mutableStateOf("") }
    val dotTimer = remember { Timer() }

    // Start the timer when isSpeaking becomes true
    LaunchedEffect(isSpeaking) {
        if (isSpeaking) {
            dotTimer.schedule(object : TimerTask() {
                override fun run() {
                    dots = if (dots.length < 3) "$dots." else ""
                }
            }, 0, 500) // Update dots every 500ms
        } else {
            dotTimer.cancel() // Cancel the timer when isSpeaking is false
            dots = "" // Reset dots
        }
    }
    Text(
        text = "Listening$dots",
        color = MaterialTheme.colorScheme.onSurface,
        style = MaterialTheme.typography.titleMedium
    )
}


@Composable
private fun SpeakButton(state: SpeechToTextState, onSpeakButtonClick: () -> Unit) {
    SecondaryButton(onClick = onSpeakButtonClick, content = {
        AnimatedContent(targetState = state.isSpeaking, label = "") { isSpeaking ->
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text =
                    if (isSpeaking) "Stop Listening"
                    else if (state.spokenText.isEmpty()) "Tap To Speak"
                    else "Speak Again?",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(vertical = 32.dp)
                )
                if (state.spokenText.isEmpty()) {
                    Icon(
                        tint = MaterialTheme.colorScheme.onSurface,
                        imageVector = if (isSpeaking) Icons.Filled.Stop else Icons.Filled.Mic,
                        contentDescription = null
                    )
                }
            }
        }
    })

}

@Preview
@Composable
private fun SpeakingScreenPreview() {
    SpellCorrectTheme {
        var isSpeaking by remember {
            mutableStateOf(false)
        }
        var showConfirmationDialogState by remember { mutableStateOf(false) }

        SpeakingContent(
            word = PreviewDataSource.singleWord().word,
            state = SpeechToTextState(isSpeaking = isSpeaking, spokenText = ""),
            hasPermission = true,
            start = {
                isSpeaking = true
            },
            stop = {
                isSpeaking = false
            },
            progress = { 0.7f },
            onSubmitClicked = {},
            showConfirmationDialogState = {
                showConfirmationDialogState = it
            },
            showConfirmationDialog = showConfirmationDialogState,
            navController = rememberNavController(),
            reset = {},
            sectionId = 0
        )
    }
}