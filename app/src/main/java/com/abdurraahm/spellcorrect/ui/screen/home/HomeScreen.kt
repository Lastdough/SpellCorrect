package com.abdurraahm.spellcorrect.ui.screen.home

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateMap
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.abdurraahm.spellcorrect.data.local.model.BottomSheetButtonData
import com.abdurraahm.spellcorrect.data.local.model.Mode
import com.abdurraahm.spellcorrect.data.local.model.Section
import com.abdurraahm.spellcorrect.data.local.model.SectionData
import com.abdurraahm.spellcorrect.data.local.model.WordEntry
import com.abdurraahm.spellcorrect.data.preview.PreviewDataSource
import com.abdurraahm.spellcorrect.ui.navigation.DefaultBottomSheet
import com.abdurraahm.spellcorrect.ui.component.SectionCard
import com.abdurraahm.spellcorrect.ui.navigation.DefaultBottomBar
import com.abdurraahm.spellcorrect.ui.navigation.DefaultTopBar
import com.abdurraahm.spellcorrect.ui.navigation.Screen
import com.abdurraahm.spellcorrect.ui.screen.home.component.TopContent
import com.abdurraahm.spellcorrect.ui.screen.home.component.WordOfTheDay
import com.abdurraahm.spellcorrect.ui.screen.loading.CircularLoading
import com.abdurraahm.spellcorrect.ui.state.UiState
import com.abdurraahm.spellcorrect.ui.theme.SpellCorrectTheme

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    homeViewModel: HomeViewModel = hiltViewModel()
) {
    val listOfSectionState by homeViewModel.listOfSection.collectAsState()
    when (val wordOfTheDayState = homeViewModel.wordOfTheDay.collectAsState().value) {
        UiState.Loading -> CircularLoading()
        is UiState.Error -> {}
        is UiState.Success -> {
            HomeContent(
                modifier = modifier,
                navController = navController,
                wordOfTheDay = wordOfTheDayState.data,
                listOfSection = listOfSectionState,
                onWordOfTheDayExerciseClicked = {
                    navController.navigate(
                        Screen.FlashScreen.createRoute(
                            mode = Mode.WORD.ordinal,
                            sectionId = wordOfTheDayState.data.section.ordinal,
                        )
                    )
                },
                onSpeakWord = homeViewModel::speak // Pass the speak function
            )
        }

        UiState.Empty -> TODO()
    }
}

@Composable
private fun HomeContent(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    wordOfTheDay: WordEntry,
    listOfSection: UiState<List<SectionData>>,
    onWordOfTheDayExerciseClicked: () -> Unit,
    onSpeakWord: (String) -> Unit
) {
    var showWordOfTheDayBottomSheet by remember { mutableStateOf(false) }
    val showSectionBottomSheetMap = remember { mutableStateMapOf<Section, Boolean>() }

    Scaffold(
        modifier = modifier,
        topBar = { DefaultTopBar() },
        bottomBar = { DefaultBottomBar(navController = navController) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                TopContent(
                    modifier = Modifier.padding(bottom = 16.dp),
                    sectionFinished = when (listOfSection) {
                        is UiState.Success -> listOfSection.data.count { it.finished }
                        else -> 0 // Handle loading or error state
                    },
                    totalSection = when (listOfSection) {
                        is UiState.Success -> listOfSection.data.size
                        else -> 0 // Handle loading or error state
                    }
                )
                WordOfTheDay(
                    modifier = Modifier.padding(bottom = 16.dp),
                    word = wordOfTheDay,
                    onWordOfTheDayClicked = { showWordOfTheDayBottomSheet = true }
                )
            }
            when (listOfSection) {
                is UiState.Success -> {
                    items(listOfSection.data, key = { it.id }) { section ->
                        SectionCard(
                            section = section,
                            onSectionClicked = {
                                showSectionBottomSheetMap[it.partSection] = true
                            }
                        )
                    }
                }

                is UiState.Error -> {
                    item {
                        Text("Error loading sections")
                    }
                }

                UiState.Loading -> {
                    item {
                        CircularLoading()
                    }
                }

                UiState.Empty -> TODO()
            }
        }
    }

    // Call DefaultBottomSheet for word of the day
    DefaultBottomSheet(
        showBottomSheet = showWordOfTheDayBottomSheet,
        onBottomSheetDismissRequest = { showWordOfTheDayBottomSheet = false },
        title = "Today Word Of The Day\n${wordOfTheDay.wordFirstLetterCapitalized}",
        buttonData = listOf(
            BottomSheetButtonData("Description") { onSpeakWord(wordOfTheDay.fullDescription) },
            BottomSheetButtonData("Exercise", onClick = {
                showWordOfTheDayBottomSheet = false
                onWordOfTheDayExerciseClicked()
            }),
            BottomSheetButtonData("Review") { /* Third button action */ }
        )
    )

    // Call DefaultBottomSheet for each section
    when (listOfSection) {
        is UiState.Success -> {
            listOfSection.data.forEach { section ->
                DefaultBottomSheet(
                    showBottomSheet = showSectionBottomSheetMap[section.partSection] ?: false,
                    onBottomSheetDismissRequest = {
                        showSectionBottomSheetMap[section.partSection] = false
                    },
                    title = "Section ${section.partInRomanNumeral}",
                    buttonData = sectionBottomSheetButtonData(
                        showSectionBottomSheetMap = showSectionBottomSheetMap,
                        section = section,
                        navController = navController
                    )
                )
            }
        }

        is UiState.Error -> {}
        UiState.Loading -> {
            CircularLoading()
        }
        UiState.Empty -> TODO()
    }
}

private fun sectionBottomSheetButtonData(
    modifier: Modifier = Modifier,
    showSectionBottomSheetMap: SnapshotStateMap<Section, Boolean>,
    section: SectionData,
    navController: NavHostController
): MutableList<BottomSheetButtonData> = mutableListOf<BottomSheetButtonData>().apply {
    if (section.finished) {
        add(0, BottomSheetButtonData("Search Specific Word") {})
        add(0, BottomSheetButtonData("Start Over") {
            showSectionBottomSheetMap[section.partSection] = false
            navController.navigate(
                Screen.FlashScreen.createRoute(
                    sectionId = section.id,
                    mode = Mode.START.ordinal
                )
            )
        })
    } else if (section.started) {
        add(0, BottomSheetButtonData("Start Over") {
            showSectionBottomSheetMap[section.partSection] = false
            navController.navigate(
                Screen.FlashScreen.createRoute(
                    sectionId = section.id,
                    mode = Mode.START.ordinal
                )
            )
        })
        add(0, BottomSheetButtonData("Search Specific Word") {})
        add(0, BottomSheetButtonData("Continue Last Session") {
            showSectionBottomSheetMap[section.partSection] = false
            navController.navigate(
                Screen.FlashScreen.createRoute(
                    sectionId = section.id,
                    mode = Mode.RESUME.ordinal
                )
            )
        })
    } else {
        add(0, BottomSheetButtonData("Search Specific Word") {})
        add(0, BottomSheetButtonData("Start Section") {
            showSectionBottomSheetMap[section.partSection] = false
            navController.navigate(
                Screen.FlashScreen.createRoute(
                    sectionId = section.id,
                    mode = Mode.START.ordinal
                )
            )
        })

    }
}

@Preview
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun HomeContentPreview() {
    SpellCorrectTheme {
        HomeContent(
            wordOfTheDay = PreviewDataSource.singleWord(),
            listOfSection = UiState.Success(PreviewDataSource.section().take(3)),
            navController = rememberNavController(),
            onSpeakWord = {},
            onWordOfTheDayExerciseClicked = {}
        )
    }
}