package com.promptimizer

import android.app.Activity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.coroutines.*
import com.promptimizer.internal.Event
import com.promptimizer.internal.Firebase
import com.promptimizer.internal.Logger
import com.promptimizer.internal.NoPrompt
import com.promptimizer.internal.PromptsImpl.showInAppRating
import com.promptimizer.internal.PromptsImpl.showSentimentPrompt

object Promptimizer {

    sealed class Options {
        data class Firebase(
            val remoteConfig: FirebaseRemoteConfig,
            val analytics: FirebaseAnalytics?
        ) : Options()
    }

    internal interface Backend {
        suspend fun getPrompt(location: String): Prompt
        fun track(event: Event)
    }

    private var backend: Backend? = null

    private var initialized = false

    private val logger: Logger = Logger()

    private val exceptionHandler = CoroutineExceptionHandler { _, exception ->
        logger.log(exception)
    }

    fun configure(
        remoteConfig: FirebaseRemoteConfig,
        analytics: FirebaseAnalytics? = null,
        onConfigureCompleteListener: onComplete? = null
    ) {
        catchExceptionsAndLog {
            configure(
                Options.Firebase(remoteConfig, analytics),
                onConfigureCompleteListener
            )
        }
    }

    fun configure(
        options: Options,
        onConfigureCompleteListener: onComplete? = null
    ) {
        catchExceptionsAndLog {
            if (initialized) {
                onConfigureCompleteListener?.invoke(null)
            } else {
                when (options) {
                    is Options.Firebase -> backend = Firebase(options)
                }
                initialized = true
                onConfigureCompleteListener?.invoke(null)
            }
        }
    }

    fun getPrompt(
        location: String,
        onPromptReady: OnPromptReady? = null
    ) {
        catchExceptionsAndLog {
            if (!initialized) {
                onPromptReady?.invoke(Error.SdkNotInitialized, NoPrompt(location))
            } else {
                CoroutineScope(Dispatchers.IO).launch(exceptionHandler) {
                    val promptToShow = backend?.getPrompt(location) ?: NoPrompt(location)
                    withContext(Dispatchers.Main) {
                        onPromptReady?.invoke(null, promptToShow)
                    }
                }
            }
        }
    }

    fun showPrompt(activity: Activity, prompt: Prompt, onPromptComplete: OnPromptComplete? = null) {
        catchExceptionsAndLog {
            when (prompt) {
                is Prompt.Sentiment -> {
                    val optionalBackend = backend
                    if (optionalBackend == null) {
                        onPromptComplete?.invoke(Error.SdkNotInitialized, prompt)
                    } else {
                        showSentimentPrompt(activity, optionalBackend, prompt, onPromptComplete)
                    }
                }
                is Prompt.Rating -> {
                    val optionalBackend = backend
                    if (optionalBackend == null) {
                        onPromptComplete?.invoke(Error.SdkNotInitialized, prompt)
                    } else {
                        showInAppRating(activity, optionalBackend, prompt, onPromptComplete)
                    }
                }
                is Prompt.None -> {
                    onPromptComplete?.invoke(null, prompt)
                }
            }
        }
    }

    fun track(
        event: String,
        parameters: Map<String, Any>? = null,
        onComplete: onComplete? = null
    ) {
        catchExceptionsAndLog {
            if (!initialized) {
                onComplete?.invoke(Error.SdkNotInitialized)
            } else {
                val backendEvent = Event.Custom(event, parameters)
                backend?.track(backendEvent)
                onComplete?.invoke(null)
            }
        }
    }

    private fun catchExceptionsAndLog(lambda: () -> Unit) {
        try {
            lambda()
        } catch (throwable: Throwable) {
            logger.log(throwable)
        }
    }
}

