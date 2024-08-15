package com.abdurraahm.spellcorrect.ui.screen.review.listening

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.abdurraahm.spellcorrect.data.local.model.Section
import com.abdurraahm.spellcorrect.data.local.model.WordEntry
import com.abdurraahm.spellcorrect.data.preview.PreviewDataSource
import com.abdurraahm.spellcorrect.ui.component.SecondaryButton
import com.abdurraahm.spellcorrect.ui.navigation.ProgressTopBar
import com.abdurraahm.spellcorrect.ui.navigation.Screen
import com.abdurraahm.spellcorrect.ui.screen.loading.CircularLoading
import com.abdurraahm.spellcorrect.ui.screen.review.ReviewViewModel
import com.abdurraahm.spellcorrect.ui.state.UiState
import com.abdurraahm.spellcorrect.ui.theme.SpellCorrectTheme

@Composable
fun ListeningScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    sectionId: Int,
    reviewViewModel: ReviewViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        reviewViewModel.get10WordsOnSection(Section.entries[sectionId])
    }

    val context = LocalContext.current
    val listOfWords: UiState<List<WordEntry>> =
        reviewViewModel.listOf10RandomWords.collectAsState().value
    var currentIndex by remember { mutableIntStateOf(0) }
    var showConfirmationDialogState by remember { mutableStateOf(false) }
    var correctAnswers by remember { mutableIntStateOf(0) }

    var userInput by remember { mutableStateOf("") }

    when (listOfWords) {
        UiState.Empty -> {}
        is UiState.Error -> {}
        UiState.Loading -> CircularLoading()
        is UiState.Success -> {
            val words = listOfWords.data
            val word = words[currentIndex]
            ListeningContent(
                progress = { currentIndex.toFloat() / words.size },
                word = word,
                onSubmitClick = {
                    val typedText = userInput.trim().lowercase()
                    val currentWord = word.word.lowercase()

                    if (typedText == currentWord) {
                        // Words match, handle as correct
                        correctAnswers++
                        userInput = ""
                        Toast.makeText(context, "Correct!", Toast.LENGTH_SHORT).show()
                    } else {
                        // Words don't match, handle as incorrect
                        userInput = ""
                        Toast.makeText(context, "Try again", Toast.LENGTH_SHORT).show()
                    }

                    if (currentIndex < words.size - 1) {
                        currentIndex++
                        Toast.makeText(context, "$currentIndex / ${words.size}", Toast.LENGTH_SHORT)
                            .show()
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
                speak = reviewViewModel::speak,
                userInput = userInput,
                onUserInputChange = {
                    userInput = it
                },
                navController = navController
            )
        }
    }

}

@Composable
private fun ListeningContent(
    userInput: String,
    onUserInputChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    word: WordEntry,
    progress: () -> Float,
    onSubmitClick: () -> Unit,
    showConfirmationDialog: Boolean,
    showConfirmationDialogState: (Boolean) -> Unit,
    speak: (String) -> Unit,
    navController: NavHostController
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

    Scaffold(modifier = modifier,
        topBar = {
            ProgressTopBar(progress = progress) {
                showConfirmationDialogState(true)
            }
        },
        bottomBar = { }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Listen and Type...", style = MaterialTheme.typography.titleLarge
            )
            SecondaryButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = { speak(word.fullDescription) }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                    contentDescription = null
                )
            }
            OutlinedTextField(
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White, // White for unfocused state
                    focusedContainerColor = Color.White    // White for focused state
                    // Customize other colors as needed...
                ),
                modifier = Modifier.fillMaxWidth(),
                value = userInput,
                onValueChange = {
                    onUserInputChange(it)
                }
            )
            AnimatedContent(targetState = userInput.isNotEmpty(), label = "") { enabled ->
                if (enabled) {
                    SecondaryButton(
                        modifier = Modifier
                            .fillMaxWidth(), // Apply the scale modifier
                        onClick = onSubmitClick
                    ) {
                        Text(
                            modifier = Modifier.padding(vertical = 6.dp),
                            text = "Next",
                            style = MaterialTheme.typography.titleMedium
                        )
                    }
                }
            }

        }
    }
}

@Preview
@Composable
private fun ListeningScreenPreview() {
    SpellCorrectTheme {
        var showConfirmationDialogState by remember { mutableStateOf(false) }
        var userInput by remember { mutableStateOf("") }


        ListeningContent(
            word = PreviewDataSource.singleWord(),
            progress = { 0.343f },
            onSubmitClick = {},
            showConfirmationDialog = showConfirmationDialogState,
            showConfirmationDialogState = {
                showConfirmationDialogState = it
            },
            speak = {},
            userInput = userInput,
            onUserInputChange = {
                userInput = it
            },
            navController = rememberNavController()
        )
    }
}