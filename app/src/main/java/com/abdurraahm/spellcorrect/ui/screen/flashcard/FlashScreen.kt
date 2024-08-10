package com.abdurraahm.spellcorrect.ui.screen.flashcard

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
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
    LaunchedEffect(Unit) {
        when (Exercise.entries[exerciseState]) {
            Exercise.START -> flashViewModel.startExercise(Section.entries[sectionId])
            Exercise.RESUME -> flashViewModel.resumeExercise(Section.entries[sectionId])
        }
    }
    val context = LocalContext.current
    when(val shuffledWords = flashViewModel.shuffledWords.collectAsState().value){
        is UiState.Error -> {
        }
        UiState.Loading -> {
            CircularLoading()
        }
        is UiState.Success -> {
            FlashContent(
                modifier = modifier,
                navController = navController,
                list = shuffledWords.data,
                i = sectionId
            )
        }
    }


}

@Composable
private fun FlashContent(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    i: Int,
    list: List<WordEntry>
) {
    Scaffold(modifier = modifier) {
        // Content
        Column(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxSize(),
        ) {
            Text(text = "Flash Card $i")
            Text(text = list.take(1).toString())
        }
    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun FlashContentPreview() {
    SpellCorrectTheme {
        FlashContent(
            navController = rememberNavController(), i = 0,
            list = listOf(PreviewDataSource.singleWord())
        )
    }
}