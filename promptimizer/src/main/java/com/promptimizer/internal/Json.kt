package com.promptimizer.internal

import com.promptimizer.Prompt
import org.json.JSONObject

internal object Json {

    fun decodePrompt(promptJson: String, logger: Logger): Prompt {
        return try {
            val jsonObject = JSONObject(promptJson)
            val locationKey = "location"
            when (jsonObject.optString("type")) {
                "sentiment" -> {
                    val location = jsonObject.optString(locationKey)
                    val title = jsonObject.optString("title")
                    val positiveButton = jsonObject.optString("positive_button")
                    val negativeButton = jsonObject.optString("negative_button")
                    Prompt.Sentiment(location, title, positiveButton, negativeButton)
                }
                "rating" -> {
                    val location = jsonObject.optString(locationKey)
                    Prompt.Rating(location)
                }
                "none" -> {
                    val location = jsonObject.optString(locationKey)
                    Prompt.None(location)
                }
                else -> {
                    Prompt.None(location = "")
                }
            }
        } catch (throwable: Throwable) {
            logger.log(throwable)
            Prompt.None(location = "")
        }
    }
}