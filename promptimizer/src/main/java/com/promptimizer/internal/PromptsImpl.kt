package com.promptimizer.internal

import android.app.Activity
import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.play.core.review.ReviewManagerFactory
import com.promptimizer.Error
import com.promptimizer.OnPromptComplete
import com.promptimizer.Prompt
import com.promptimizer.Promptimizer

internal data class NoPrompt(override val location: String) : Prompt.None()

internal object PromptsImpl {

    fun showSentimentPrompt(
        context: Context,
        backend: Promptimizer.Backend,
        sentimentPrompt: Prompt.Sentiment,
        onPromptComplete: OnPromptComplete?
    ) {
        MaterialAlertDialogBuilder(context)
            .setTitle(sentimentPrompt.title)
            .setNegativeButton(sentimentPrompt.negativeButton) { _, _ ->
                backend.track(Event.SentimentPromptNegativeButton)
                onPromptComplete?.invoke(null, sentimentPrompt)
            }
            .setPositiveButton(sentimentPrompt.positiveButton) { _, _ ->
                backend.track(Event.SentimentPromptPositiveButton)
                onPromptComplete?.invoke(null, sentimentPrompt)
            }
            .show()
        backend.track(Event.SentimentPromptDisplayed)
    }

    fun showInAppRating(
        activity: Activity,
        backend: Promptimizer.Backend,
        ratingPrompt: Prompt.Rating,
        onPromptComplete: OnPromptComplete?
    ) {
        val appContext = activity.applicationContext
        val manager = ReviewManagerFactory.create(appContext)
        val request = manager.requestReviewFlow()

        backend.track(Event.StartedRatingFlow)
        request.addOnCompleteListener { result ->
            if (result.isSuccessful) {
                val reviewInfo = result.result
                val flow = manager.launchReviewFlow(activity, reviewInfo)
                flow.addOnCompleteListener {
                    backend.track(Event.CompletedRatingFlow)
                    onPromptComplete?.invoke(null, ratingPrompt)
                }
            } else {
                val exception = result.exception
                backend.track(Event.InAppRatingFailure)
                onPromptComplete?.invoke(
                    Error.InAppRatingFailure(
                        exception?.message ?: "In App Rating Failure"
                    ), ratingPrompt
                )
            }
        }
    }
}