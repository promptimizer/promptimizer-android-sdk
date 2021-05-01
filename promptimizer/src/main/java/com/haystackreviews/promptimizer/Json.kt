package com.haystackreviews.promptimizer

import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

val format = Json {
    serializersModule = SerializersModule {
        polymorphic(Prompt::class) {
            subclass(SentimentPrompt::class)
            subclass(RatingPrompt::class)
            default { NoPrompt.serializer() }
        }
    }

}