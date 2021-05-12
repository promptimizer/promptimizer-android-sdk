package com.haystackreviews.promptimizer

sealed class Error(val id: Int, open val message: String)
object SdkNotInitialized : Error(0, "SDK Not Initialized")
class InAppRatingFailure(override val message: String) : Error(1, message)