package promptimizer.internal

import android.util.Log


internal class Logger {

    private val promptimizerTag = "Promptimizer"

    fun log(throwable: Throwable) {
        Log.e(promptimizerTag, "Internal SDK exception caught. Please report this exception on Github.", throwable)
    }
}