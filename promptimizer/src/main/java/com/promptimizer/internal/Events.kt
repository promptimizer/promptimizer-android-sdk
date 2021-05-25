package com.promptimizer.internal

internal sealed class Event(open val event: String, open val parameters: Map<String, Any>? = null) {
    data class Custom(override val event: String, override val parameters: Map<String, Any>?) :
        Event(event, parameters)

    object SentimentPromptDisplayed : Event("sentimentPromptDisplayed")
    object SentimentPromptNegativeButton : Event("sentimentPromptNegativeButton")
    object SentimentPromptPositiveButton : Event("sentimentPromptPositiveButton")
    object StartedRatingFlow : Event("startedRatingFlow")
    object InAppRatingFailure : Event("inAppRatingFailure")
    object CompletedRatingFlow : Event("completedRatingFlow")
}