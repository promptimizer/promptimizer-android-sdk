package com.haystackreviews.promptimizer

import android.app.Activity
import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.play.core.review.ReviewManagerFactory
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class Prompt {
    abstract val location: String
}

@Serializable
@SerialName("sentiment")
data class SentimentPrompt(
    @SerialName("location")
    override val location: String,
    @SerialName("title")
    val title: String,
    @SerialName("positive_button")
    val positiveButton: String,
    @SerialName("negative_button")
    val negativeButton: String,
) : Prompt()

@Serializable
@SerialName("rating")
data class RatingPrompt(
    @SerialName("location")
    override val location: String
) : Prompt()

@Serializable
@SerialName("none")
data class NoPrompt(
    @SerialName("location")
    override val location: String
) : Prompt()

fun showSentimentPrompt(
    context: Context,
    backend: Promptimizer.Backend,
    sentimentPrompt: SentimentPrompt,
    onPromptComplete: OnPromptComplete?
) {
    MaterialAlertDialogBuilder(context)
        .setTitle(sentimentPrompt.title)
        .setNegativeButton(sentimentPrompt.negativeButton) { _, _ ->
            backend.track(sentimentPromptNegativeButton)
            onPromptComplete?.invoke(null, sentimentPrompt)
        }
        .setPositiveButton(sentimentPrompt.positiveButton) { _, _ ->
            backend.track(sentimentPromptPositiveButton)
            onPromptComplete?.invoke(null, sentimentPrompt)
        }
        .show()
    backend.track(sentimentPromptDisplayed)
}

fun showInAppRating(
    activity: Activity,
    backend: Promptimizer.Backend,
    ratingPrompt: RatingPrompt,
    onPromptComplete: OnPromptComplete?
) {
    val appContext = activity.applicationContext
    val manager = ReviewManagerFactory.create(appContext)
    val request = manager.requestReviewFlow()

    backend.track(startedRatingFlow)
    request.addOnCompleteListener { result ->
        if (result.isSuccessful) {
            val reviewInfo = result.result
            val flow = manager.launchReviewFlow(activity, reviewInfo)
            flow.addOnCompleteListener {
                backend.track(completedRatingFlow)
                onPromptComplete?.invoke(null, ratingPrompt)
            }
        } else {
            val exception = result.exception
            backend.track(inAppRatingFailure)
            onPromptComplete?.invoke(InAppRatingFailure(exception?.message ?: "In App Rating Failure"), ratingPrompt)
        }
    }
}