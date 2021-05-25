package promptimizer.internal

import android.os.Bundle
import kotlinx.serialization.decodeFromString
import promptimizer.internal.Json.format
import promptimizer.Prompt
import promptimizer.Promptimizer

internal class Firebase(private val config: Promptimizer.Options.Firebase) : Promptimizer.Backend {

    override suspend fun getPrompt(location: String): Prompt {
        val promptJson = config.remoteConfig.getString("promptToShow")
        return if (promptJson.isEmpty()) {
            NoPrompt(location)
        } else {
            val decodedPrompt = format.decodeFromString<Prompt>(promptJson)
            if (location == decodedPrompt.location) {
                decodedPrompt
            } else {
                NoPrompt(location)
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