package com.abdurraahm.spellcorrect.data.service

import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.speech.tts.Voice
import android.util.Log
import java.util.Locale
import javax.inject.Inject


class TextToSpeechService @Inject constructor() : Service(), TextToSpeech.OnInitListener {

    companion object {
        private const val TAG = "TextToSpeechService"
    }

    @Inject
    lateinit var tts: TextToSpeech

    private val progressListener = object : UtteranceProgressListener() {
        override fun onStart(utteranceId: String) {
            Log.d(TAG, "Started utterance $utteranceId")
        }

        override fun onDone(utteranceId: String) {
            Log.d(TAG, "Done with utterance $utteranceId")
        }

        override fun onError(utteranceId: String?) {}
    }

    override fun onCreate() {
        super.onCreate()
        tts = TextToSpeech(this, this)
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            tts.setOnUtteranceProgressListener(progressListener)

            val voices = tts.voices
//            for (voice in voices) {
//                Log.d(TAG, "Available Voice Name: ${voice.name}")
//                Log.d(TAG, "Locale: ${voice.locale}")
//                Log.d(TAG, "Quality: ${voice.quality}")
//                Log.d(TAG, "Latency: ${voice.latency}")
//                Log.d(TAG, "Requires Network: ${voice.isNetworkConnectionRequired}")
//                Log.d(TAG, "Features: ${voice.features}")
//            }

            // Desired voice and fallback voice names
            val desiredVoiceName = "en-GB-language"
            val fallbackVoiceName = "en-gb-x-gbb-network"

            // Find the desired or fallback voice
            val desiredVoice = voices.find { it.name == desiredVoiceName }
            val fallbackVoice = voices.find { it.name == fallbackVoiceName }

            if (desiredVoice != null) {
                tts.voice = desiredVoice
                Log.d(TAG, "Using desired voice: ${desiredVoice.name}")
            } else if (fallbackVoice != null) {
                tts.voice = fallbackVoice
                Log.d(TAG, "Using fallback voice: ${fallbackVoice.name}")
            } else {
                Log.d(TAG, "Desired and fallback voices not found, using default voice.")
                tts.voice = tts.defaultVoice
            }


//            val voiceName = "en-us-x-iog-local"
//            val voiceNameGB = "en-GB-language"
//            val locale = Locale.getDefault()
//            val quality = Voice.QUALITY_HIGH
//            val latency = Voice.LATENCY_NORMAL
//            val requiresNetworkConnection = true
//            val features: Set<String> = HashSet()
//            val voice1 = Voice(voiceNameGB, locale, quality, latency, requiresNetworkConnection, features)
//            tts.voice = voice1
//            Log.d(TAG, "Using voice: ${voice1.name}")
//
//            val voice2 = tts.voices.find { it.name == voiceNameGB } ?: tts.defaultVoice

            val desiredLocale = Locale.UK // Change to the desired language/locale
            tts.setLanguage(desiredLocale)

//            val voice3 = tts.voices.find { it.name == voiceNameGB }


//            val voices = tts.voices
//            val voiceList: List<Voice> = ArrayList(voices)
//            val selectedVoice = voiceList[10] // Change to the desired voice index
//            tts.setVoice(voice1)
//            tts.setSpeechRate(2.0f)
//
//            if (voice3 != null) {
//                tts.voice = voice3
//                Log.d(TAG, "Using voice: ${voice3.name}")
//            } else {
//                Log.d(TAG, "Desired voice not found, using default voice.")
//                tts.voice = tts.defaultVoice
//            }

//          tts.voice =
//            tts.speak(
//                "Hello, my name is anton, every morning i usually get up at 7 clock",
//                TextToSpeech.QUEUE_ADD,
//                Bundle(),
//                "utteranceId"
//            )
        }
    }

    override fun onDestroy() {
        tts.shutdown()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    fun speak(text: String, queueMode: Int = TextToSpeech.QUEUE_FLUSH) {
        tts.speak(text, queueMode, null, null)
    }

    private fun setTTSVoice() {
        val voices: Set<Voice> = tts.voices ?: return
        for (voice in voices) {
            if (voice.name == "en-US-language") {
                tts.voice = voice
                break
            }
        }
    }
}