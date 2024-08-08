package com.abdurraahm.spellcorrect.data.service

import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.speech.tts.Voice
import android.util.Log
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
            tts.voice = tts.voices.find { it.name == "en-us-x-iog-local" } ?: tts.defaultVoice
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