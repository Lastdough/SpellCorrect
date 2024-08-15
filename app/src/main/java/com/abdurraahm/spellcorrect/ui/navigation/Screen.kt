package com.abdurraahm.spellcorrect.ui.navigation

sealed class Screen(val route: String) {
    data object OnBoarding : Screen("OnBoarding")
    data object Home : Screen("Home")
    data object Review : Screen("Review")
    data object More : Screen("More")

    data object Speaking : Screen("Speaking/{sectionId}") {
        fun createRoute(sectionId: Int) = "Speaking/$sectionId"
    }

    data object Listening : Screen("Listening/{sectionId}") {
        fun createRoute(sectionId: Int) = "Listening/$sectionId"
    }

    data object Result : Screen("result/{correctAnswers}/{totalQuestions}") {
        fun createRoute(correctAnswers: Int, totalQuestions: Int) =
            "result/${correctAnswers}/${totalQuestions}"
    }

    data object FlashScreen : Screen("flash/{mode}/{sectionId}") {
        fun createRoute(mode: Int, sectionId: Int) = "flash/$mode/$sectionId"
    }
}