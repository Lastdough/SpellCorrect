package com.abdurraahm.spellcorrect.data.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
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
        }
    }

    override fun onDestroy() {
        tts.shutdown()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}