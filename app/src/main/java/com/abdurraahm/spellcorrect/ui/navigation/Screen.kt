package com.abdurraahm.spellcorrect.ui.navigation

sealed class Screen(val route: String) {
    data object OnBoarding : Screen("OnBoarding")
    data object Home : Screen("Home")
    data object Review : Screen("Review")
    data object More : Screen("More")

    object DrugsDetails : Screen("detail/{id}") {
        fun createRoute(id: String) = "detail/$id"
    }


}