package com.abdurraahm.spellcorrect

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.abdurraahm.spellcorrect.ui.MainViewModel
import com.abdurraahm.spellcorrect.ui.SpellCorrectApp
import com.abdurraahm.spellcorrect.ui.navigation.Screen
import com.abdurraahm.spellcorrect.ui.theme.SpellCorrectTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        var keepSplashScreen = true
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition {
            keepSplashScreen
        }

        lifecycleScope.launch {
            val isEmpty = mainViewModel.isDBEmpty.first()
            Log.d(TAG, "isdbemty : $isEmpty")
            if (isEmpty) {
                mainViewModel.initDB()
                Log.d(TAG, "initDB()")
            }

            mainViewModel.onboardingCompletedState.collect { completed ->
                // splash screen false once state is known
                keepSplashScreen = false
                setContent {
                    var canRecord by remember {
                        mutableStateOf(false)
                    }

                    // Creates an permission request
                    val recordAudioLauncher = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.RequestPermission(),
                        onResult = { isGranted ->
                            canRecord = isGranted
                        }
                    )

                    LaunchedEffect(key1 = recordAudioLauncher) {
                        // Launches the permission request
                        recordAudioLauncher.launch(AUDIO_PERMISSION)
                    }

                    SpellCorrectTheme {
                        val startDestination =
                            if (completed) Screen.Home.route else Screen.OnBoarding.route
                        SpellCorrectApp(startDestination = startDestination)
                    }
                }
            }
        }
        mainViewModel.startTextToSpeech()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Destroy TTS
        mainViewModel.stopTextToSpeech()
    }

    companion object {
        const val AUDIO_PERMISSION = android.Manifest.permission.RECORD_AUDIO
        val TAG = "Main Activity saya"
    }
}

