package com.abdurraahm.spellcorrect

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.abdurraahm.spellcorrect.ui.SpellCorrectApp
import com.abdurraahm.spellcorrect.ui.theme.SpellCorrectTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpellCorrectTheme {
                SpellCorrectApp()
            }
        }
    }
}

