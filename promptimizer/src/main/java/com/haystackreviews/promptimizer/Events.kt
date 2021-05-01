package com.haystackreviews.promptimizer

data class Event(val event: String, val parameters: Map<String, Any>? = null)
val sentimentPromptDisplayed: Event get() = Event("sentimentPromptDisplayed")
val sentimentPromptNegativeButton: Event get() = Event("sentimentPromptNegativeButton")
val sentimentPromptPositiveButton: Event get() = Event("sentimentPromptPositiveButton")
val startedRatingFlow: Event get() = Event("startedRatingFlow")
val inAppRatingFailure: Event get() = Event("inAppRatingFailure")
val completedRatingFlow: Event get() = Event("completedRatingFlow")