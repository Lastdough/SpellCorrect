package com.abdurraahm.spellcorrect.ui.navigation

sealed class Screen(val route: String) {


    object OnBoarding : Screen("OnBoarding")
    object Home : Screen("Home")
    object Review : Screen("Review")
    object More : Screen("More")



    object DrugsDetails : Screen("detail/{id}") {
        fun createRoute(id: String) = "detail/$id"
    }


}