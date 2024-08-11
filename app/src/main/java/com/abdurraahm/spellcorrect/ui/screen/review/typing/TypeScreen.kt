package com.abdurraahm.spellcorrect.ui.screen.review.typing

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.abdurraahm.spellcorrect.data.local.model.SectionData

@Composable
fun TypingScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    sectionId: Int,
) {

}

@Composable
private fun TypingContent(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    listOfSection: List<SectionData>,
    onSectionCartClicked: (Int, Boolean) -> Unit
) {

}