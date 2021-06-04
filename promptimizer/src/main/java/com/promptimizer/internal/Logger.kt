package com.promptimizer.internal

import android.util.Log


internal class Logger {

    private val promptimizerTag = "Promptimizer"

    fun log(throwable: Throwable) {
        Log.e(promptimizerTag, "SDK exception caught and swallowed. Please report this stacktrace to https://github.com/promptimizer/promptimizer-android-sdk.", throwable)
    }
}