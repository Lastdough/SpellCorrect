package com.abdurraahm.spellcorrect.ui.screen.review.result

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.abdurraahm.spellcorrect.ui.navigation.Screen
import com.abdurraahm.spellcorrect.ui.theme.SpellCorrectTheme

@Composable
fun ResultScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    correctAnswers: Int,
    totalQuestions: Int,
) {
    ResultContent(
        navController = navController,
        correctAnswers = correctAnswers,
        totalQuestions = totalQuestions
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ResultContent(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    correctAnswers: Int,
    totalQuestions: Int,
) {
    Scaffold(modifier = modifier,
        topBar = {
            TopAppBar(title = { }, actions = {
                IconButton(onClick = {
                    navController.navigate(Screen.Review.route){
                    popUpTo(Screen.Review.route) { inclusive = true }
                } }) {
                    Icon(imageVector = Icons.Rounded.Close, contentDescription = null)
                }
            }
            )
        },
        bottomBar = { }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Result Screen $correctAnswers/$totalQuestions ")
        }
    }
}

@Preview
@Composable
private fun ResultScreenPreview() {
    SpellCorrectTheme {
        ResultContent(        navController = rememberNavController(),
            correctAnswers = 0,
            totalQuestions = 1)
    }
}