package com.abdurraahm.spellcorrect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.abdurraahm.spellcorrect.ui.MainViewModel
import com.abdurraahm.spellcorrect.ui.SpellCorrectApp
import com.abdurraahm.spellcorrect.ui.navigation.Screen
import com.abdurraahm.spellcorrect.ui.theme.SpellCorrectTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        var keepSplashScreen = true
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition{
            keepSplashScreen
        }

        val mainViewModel: MainViewModel by viewModels()
        lifecycleScope.launch {
            mainViewModel.onboardingCompletedState.collect { completed ->
                // splash screen false once state is known
                keepSplashScreen = false
                setContent {
                    SpellCorrectTheme {
                        val startDestination = if (completed) Screen.Home.route else Screen.OnBoarding.route
                        SpellCorrectApp(startDestination = startDestination)
                    }
                }
            }
        }
    }
}

