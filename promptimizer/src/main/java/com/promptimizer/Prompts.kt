package com.promptimizer

import androidx.annotation.Keep
import kotlinx.serialization.Serializable

@Keep
@Serializable
sealed class Prompt {
    abstract val location: String

    abstract class Sentiment : Prompt() {
        abstract override val location: String
        abstract val title: String
        abstract val positiveButton: String
        abstract val negativeButton: String
    }

    abstract class Rating : Prompt() {
        abstract override val location: String
    }

    abstract class None : Prompt() {
        abstract override val location: String
    }
}