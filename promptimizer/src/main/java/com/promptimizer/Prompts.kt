package com.promptimizer

sealed class Prompt {
    abstract val location: String

    data class Sentiment(
        override val location: String,
        val title: String,
        val positiveButton: String,
        val negativeButton: String
    ) : Prompt()

    data class Rating(override val location: String) : Prompt()

    data class None(override val location: String) : Prompt()
}