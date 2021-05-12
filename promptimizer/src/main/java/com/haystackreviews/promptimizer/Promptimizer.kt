package com.haystackreviews.promptimizer

import android.app.Activity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

object Promptimizer {

    sealed class Options {
        data class Firebase(
            val remoteConfig: FirebaseRemoteConfig,
            val analytics: FirebaseAnalytics?
        ) : Options()
    }

    interface Backend {
        suspend fun getPrompt(location: String): Prompt
        fun track(event: Event)
    }

    private var backend: Backend? = null

    private var initialized = false

    fun configure(
        remoteConfig: FirebaseRemoteConfig,
        analytics: FirebaseAnalytics? = null,
        onConfigureCompleteListener: onComplete? = null
    ) {
        configure(Options.Firebase(remoteConfig, analytics), onConfigureCompleteListener)
    }

    fun configure(
        options: Options,
        onConfigureCompleteListener: onComplete? = null
    ) {
        if (initialized) {
            onConfigureCompleteListener?.invoke(null)
            return
        }
        when (options) {
            is Options.Firebase -> backend = Firebase(options)
        }
        initialized = true
        onConfigureCompleteListener?.invoke(null)
    }

    fun getPrompt(
        location: String,
        onPromptReady: OnPromptReady? = null
    ) {
        if (!initialized) {
            onPromptReady?.invoke(SdkNotInitialized, NoPrompt(location = location))
            return
        }
        CoroutineScope(Dispatchers.IO).launch {
            val promptToShow = backend?.getPrompt(location) ?: NoPrompt(location = location)
            withContext(Dispatchers.Main) {
                onPromptReady?.invoke(null, promptToShow)
            }
        }
    }

    fun showPrompt(activity: Activity, prompt: Prompt, onPromptComplete: OnPromptComplete? = null) {
        when (prompt) {
            is SentimentPrompt -> {
                val optionalBackend = backend
                if (optionalBackend == null) {
                    onPromptComplete?.invoke(SdkNotInitialized, prompt)
                } else {
                    showSentimentPrompt(activity, optionalBackend, prompt, onPromptComplete)
                }
            }
            is RatingPrompt -> {
                val optionalBackend = backend
                if (optionalBackend == null) {
                    onPromptComplete?.invoke(SdkNotInitialized, prompt)
                } else {
                    showInAppRating(activity, optionalBackend, prompt, onPromptComplete)
                }
            }
            is NoPrompt -> {
                onPromptComplete?.invoke(null, prompt)
            }
        }
    }

    fun track(
        event: String,
        parameters: Map<String, Any>? = null,
        onComplete: onComplete? = null
    ) {
        if (!initialized) {
            onComplete?.invoke(SdkNotInitialized)
        } else {
            val backendEvent = Event(event, parameters)
            backend?.track(backendEvent)
            onComplete?.invoke(null)
        }
    }
}

