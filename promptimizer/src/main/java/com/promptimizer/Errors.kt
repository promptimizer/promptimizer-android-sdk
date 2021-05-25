package com.promptimizer

sealed class Error(open val message: String) {
    object SdkNotInitialized : Error("SDK Not Initialized")
    class InAppRatingFailure(override val message: String) : Error(message)
}
