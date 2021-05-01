package com.haystackreviews.promptimizer

import android.app.Activity
import android.content.Context
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString

data class PromptimizerOptions(val firebaseConfig: FirebaseConfig)
data class FirebaseConfig(val projectId: String, val applicationId: String, val apiKey: String)

object Promptimizer {

    private var initialized = false

    fun configure(
        context: Context,
        options: PromptimizerOptions,
        onConfigureCompleteListener: OnConfigure
    ) {
        if (initialized) {
            onConfigureCompleteListener.invoke(null)
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            initializeFirebase(context, options)
            initialized = true
            withContext(Dispatchers.Main) {
                onConfigureCompleteListener.invoke(null)
            }
        }
    }

    fun getPrompt(
        location: String,
        onPromptReady: OnPromptReady
    ) {

        if (!initialized) {
            onPromptReady.invoke(SdkNotInitializedError, NoPrompt(location = location))
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            val promptJson = remoteConfig.getString("promptToShow")
            val decodedPrompt = format.decodeFromString<Prompt>(promptJson)
            val promptToShow = if (location == decodedPrompt.location) {
                decodedPrompt
            } else {
                NoPrompt(location = location)
            }

            withContext(Dispatchers.Main) {
                onPromptReady.invoke(null, promptToShow)
            }
        }
    }

    fun showPrompt(activity: Activity, prompt: Prompt, onPromptComplete: OnPromptComplete) {
        when (prompt) {
            is SentimentPrompt -> showSentimentPrompt(activity, prompt, onPromptComplete)
            is RatingPrompt -> showInAppRating(activity, prompt, onPromptComplete)
            is NoPrompt -> {
                onPromptComplete.invoke(null, prompt)
            }
        }
    }

    fun track(
        event: String,
        parameters: Map<String, Any>? = null,
        onComplete: OnConfigure? = null
    ) {
        if (!initialized) {
            onComplete?.invoke(SdkNotInitializedError)
        } else {
            val firebaseEvent = Event(event, parameters)
            trackFirebaseEvent(firebaseEvent)
            onComplete?.invoke(null)
        }
    }
}

