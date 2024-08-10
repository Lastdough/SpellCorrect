package com.abdurraahm.spellcorrect.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.abdurraahm.spellcorrect.ui.navigation.Screen
import com.abdurraahm.spellcorrect.ui.screen.flashcard.FlashScreen
import com.abdurraahm.spellcorrect.ui.screen.home.HomeScreen
import com.abdurraahm.spellcorrect.ui.screen.more.MoreScreen
import com.abdurraahm.spellcorrect.ui.screen.onboarding.OnBoardingScreen
import com.abdurraahm.spellcorrect.ui.screen.review.ReviewScreen

@Composable
fun SpellCorrectApp(
    modifier: Modifier = Modifier,
    mainViewModel: MainViewModel = hiltViewModel(),
    navController: NavHostController = rememberNavController(),
    startDestination: String
) {
    NavHost(
        modifier = modifier.fillMaxSize(),
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.OnBoarding.route) {
            OnBoardingScreen(
                onClickGetStartedClicked = {
                    mainViewModel.onOnboardingCompleted()
                    navController.navigate(Screen.Home.route)
                }
            )
        }
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        composable(Screen.Review.route) {
            ReviewScreen(navController = navController)
        }
        composable(Screen.More.route) {
            MoreScreen(navController = navController)
        }
        composable(
            route = Screen.FlashScreen.route,
            arguments = listOf(
                navArgument("sectionId") { type = NavType.IntType },
                navArgument("exerciseState") { type = NavType.IntType },
            ),
        ) {
            val sectionId = it.arguments?.getInt("sectionId") ?: 0
            val exerciseState = it.arguments?.getInt("exerciseState") ?: 0
            FlashScreen(
                navController = navController,
                sectionId = sectionId,
                exerciseState = exerciseState
            )
        }
    }
}

