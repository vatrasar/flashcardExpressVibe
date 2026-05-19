package com.example.flashcardexpress.core.data.androidTools

import android.content.Context
import android.speech.tts.TextToSpeech
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manager responsible for processing text to speech.
 */
@Singleton
class TTSManager @Inject constructor(
    @ApplicationContext private val context: Context
) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = null
    private var isInitialized = false
    private var pendingText: String? = null
    private var pendingLanguage: String? = null

    init {
        tts = TextToSpeech(context, this)
        tts?.setSpeechRate(0.5f)
    }

    /**
     * Callback for TextToSpeech initialization.
     *
     * Invoked by:
     * - [android.speech.tts.TextToSpeech]
     */
    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            isInitialized = true
            pendingText?.let { text ->
                speak(text, pendingLanguage ?: "English")
                pendingText = null
                pendingLanguage = null
            }
        } else {
            Log.e("TTSManager", "Initialization of TextToSpeech failed")
        }
    }

    /**
     * Speaks the given text in the specified language.
     *
     * Invoked by:
     * - [com.example.flashcardexpress.feature.repeat.presentation.repetition.RepetitionViewModel]
     */
    fun speak(text: String, language: String) {
        if (!isInitialized) {
            pendingText = text
            pendingLanguage = language
            return
        }

        val locale = getLocaleForLanguage(language)
        tts?.language = locale
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    /**
     * Stops any ongoing speech.
     *
     * Invoked by:
     * - [com.example.flashcardexpress.feature.repeat.presentation.repetition.RepetitionViewModel]
     */
    fun stop() {
        tts?.stop()
    }

    private fun getLocaleForLanguage(languageName: String): Locale {
        val locales = Locale.getAvailableLocales()

        val matchByDefault = locales.firstOrNull {
            it.getDisplayLanguage(Locale.getDefault()).equals(languageName, ignoreCase = true)
        }
        if (matchByDefault != null) return matchByDefault

        val matchByOwn = locales.firstOrNull {
            it.getDisplayLanguage(it).equals(languageName, ignoreCase = true)
        }
        if (matchByOwn != null) return matchByOwn

        val matchByCode = locales.firstOrNull {
            it.language.equals(languageName, ignoreCase = true)
        }
        if (matchByCode != null) return matchByCode

        return Locale.getDefault()
    }
}
