package com.abdurraahm.spellcorrect.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.abdurraahm.spellcorrect.ui.navigation.Screen
import com.abdurraahm.spellcorrect.ui.screen.onboarding.OnBoardingScreen

@Composable
fun SpellCorrectApp(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel = hiltViewModel(),
    navController: NavHostController = rememberNavController(),
    startDestination: String
) {
    val onBoardingState = mainViewModel.onboardingCompletedState.collectAsState(initial = false).value


    NavHost(
        modifier = modifier.fillMaxSize(),
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.OnBoarding.route) {
            OnBoardingScreen(
                onClickGetStartedClicked = {
                    navController.navigate(Screen.Home.route)
                    mainViewModel.onOnboardingCompleted()
                }
            )
        }
        composable(Screen.Home.route) {
            Column(
                modifier = Modifier,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Halo Ges ini Home $onBoardingState")
            }
        }
        composable(Screen.More.route) {
            Column(
                modifier = Modifier,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Halo Ges ini More $onBoardingState")
            }
        }
    }
}