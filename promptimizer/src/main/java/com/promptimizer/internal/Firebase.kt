package com.promptimizer.internal

import android.os.Bundle
import com.promptimizer.Prompt
import com.promptimizer.Promptimizer

internal class Firebase(
    private val config: Promptimizer.Options.Firebase,
    private val logger: Logger
) : Promptimizer.Backend {

    override fun getPrompt(location: String): Prompt {
        val promptJson = config.remoteConfig.getString("promptToShow")
        return if (promptJson.isEmpty()) {
            Prompt.None(location)
        } else {
            val decodedPrompt = Json.decodePrompt(promptJson, logger)
            if (location == decodedPrompt.location) {
                decodedPrompt
            } else {
                Prompt.None(location)
            }
        }
    }

    override fun track(event: Event) {
        val bundle = Bundle().apply {
            event.parameters?.forEach { (key, value) ->
                when (value) {
                    is String -> putString(key, value)
                    is Boolean -> putBoolean(key, value)
                    is Int -> putInt(key, value)
                    is Long -> putLong(key, value)
                    is Double -> putDouble(key, value)
                    is java.io.Serializable -> putSerializable(key, value)
                }
            }
        }
        config.analytics?.logEvent(event.event, bundle)
    }
}