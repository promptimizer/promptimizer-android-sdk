package promptimizer.internal

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass
import promptimizer.Prompt

internal object Json {

    val format = Json {
        serializersModule = SerializersModule {
            polymorphic(Prompt::class) {
                subclass(SentimentPromptJson::class)
                subclass(RatingPromptJson::class)
                default { NoPromptJson.serializer() }
            }
        }

    }

    @Serializable
    @SerialName("sentiment")
    data class SentimentPromptJson(
        @SerialName("location")
        override val location: String,
        @SerialName("title")
        override val title: String,
        @SerialName("positive_button")
        override val positiveButton: String,
        @SerialName("negative_button")
        override val negativeButton: String,
    ) : Prompt.Sentiment()

    @Serializable
    @SerialName("rating")
    data class RatingPromptJson(
        @SerialName("location")
        override val location: String
    ) : Prompt.Rating()

    @Serializable
    @SerialName("none")
    data class NoPromptJson(
        @SerialName("location")
        override val location: String
    ) : Prompt.None()

}