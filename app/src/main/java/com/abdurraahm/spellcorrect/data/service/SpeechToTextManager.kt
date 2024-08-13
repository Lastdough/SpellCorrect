package com.abdurraahm.spellcorrect.data.service

import android.app.Application
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.core.content.ContextCompat
import com.abdurraahm.spellcorrect.ui.state.SpeechToTextState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class SpeechToTextManager @Inject constructor(
    @ApplicationContext private val app: Application
) : RecognitionListener {
    private val recognizer = SpeechRecognizer.createSpeechRecognizer(app)
    private val _state = MutableStateFlow(SpeechToTextState())
    val state: StateFlow<SpeechToTextState>
        get() = _state.asStateFlow()

    fun hasPermission() = ContextCompat.checkSelfPermission(
        app,
        AUDIO_PERMISSION
    ) == PackageManager.PERMISSION_GRANTED

    fun resetState() = _state.update { SpeechToTextState() }

    private fun createRecognizerIntent(languageCode: String) =
        Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, languageCode)
        }

    fun startRecognizing(languageCode: String = "en") {
        if (!hasPermission()) return
        // Clears the state
        resetState()

        if (!SpeechRecognizer.isRecognitionAvailable(app)) {
            _state.update {
                it.copy(
                    error = "Speech recognition is not available"
                )
            }
        }

        val intent = createRecognizerIntent(languageCode)
        // Sets the listener that will receive all the callbacks
        recognizer.setRecognitionListener(this)

        // Starts listening for speech
        recognizer.startListening(intent)

        // Indicates that speech recognition has started
        _state.update {
            it.copy(
                isSpeaking = true
            )
        }
    }

    fun stopRecognizing() {
        // Indicates that speech recognition has stopped
        _state.update {
            it.copy(
                isSpeaking = false
            )
        }
        recognizer.stopListening()
    }

    override fun onReadyForSpeech(params: Bundle?) {
        // Speech recognition is ready
        Log.i("SpeechRecognizer", "Ready for speech")
        _state.update {
            it.copy(
                error = null
            )
        }
    }

    override fun onBeginningOfSpeech() {
        // Speech recognition has begun
        Log.i("SpeechRecognizer", "Speech recognition has begun")
    }


    override fun onRmsChanged(rmsdB: Float) {
        // Speech volume level has changed
        Log.i("SpeechRecognizer", "Speech volume level: $rmsdB dB")
    }

    override fun onBufferReceived(buffer: ByteArray?) {
        // Speech recognition data has been received
        // This does not always trigger...
    }

    override fun onEndOfSpeech() {
        // Speech recognition has ended
        Log.i("SpeechRecognizer", "Speech recognition has ended")
        _state.update {
            it.copy(
                isSpeaking = false
            )
        }
    }

    override fun onError(error: Int) {
        Log.e("SpeechRecognizer", "Error: $error")
        if (error == SpeechRecognizer.ERROR_CLIENT) {
            return
        }
        _state.update {
            it.copy(
                error = "Error: $error"
            )
        }
    }

    override fun onResults(results: Bundle?) {
        // Speech recognition results are available
        val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        val recognizedText = matches?.getOrNull(0)

        if (recognizedText != null) {
            Log.d("SpeechRecognizer", "Recognized text: $recognizedText")

            // do something with text
        }

        recognizedText?.let { text ->
            _state.update {
                it.copy(
                    spokenText = text
                )
            }
        }
    }

    // This only gets triggered with EXTRA_PARTIAL_RESULTS is set to true
    // https://developer.android.com/reference/android/speech/RecognizerIntent#EXTRA_PARTIAL_RESULTS
    override fun onPartialResults(partialResults: Bundle?) {
        // Partial speech recognition results are available
        val matches =
            partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
        val recognizedText = matches?.getOrNull(0)

        if (recognizedText != null) {
            Log.d("SpeechRecognizer", "Partial text: $recognizedText")

            // do something with text
        }
    }

    override fun onEvent(eventType: Int, params: Bundle?) {
        Log.d("SpeechRecognizer", "Event: $eventType")
    }

    companion object {
        const val AUDIO_PERMISSION = android.Manifest.permission.RECORD_AUDIO
    }
}