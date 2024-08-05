package com.abdurraahm.spellcorrect.ui.navigation

sealed class Screen(val route: String) {


    object OnBoarding : Screen("onboarding")
    object Home : Screen("home")
    object More : Screen("more")



    object DrugsDetails : Screen("detail/{id}") {
        fun createRoute(id: String) = "detail/$id"
    }


}