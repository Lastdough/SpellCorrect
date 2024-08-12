package com.abdurraahm.spellcorrect.ui.navigation

sealed class Screen(val route: String) {
    data object OnBoarding : Screen("OnBoarding")
    data object Home : Screen("Home")
    data object Review : Screen("Review")
    data object More : Screen("More")

    data object FlashScreen : Screen("flash/{mode}/{sectionId}") {
        fun createRoute(mode: Int, sectionId: Int) = "flash/$mode/$sectionId"
    }
}