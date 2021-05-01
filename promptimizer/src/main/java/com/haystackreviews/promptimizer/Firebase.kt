package com.haystackreviews.promptimizer

import android.content.Context
import android.os.Bundle
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.app
import com.google.firebase.ktx.initialize
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings

const val firebaseAppName = "promptimizer"

val remoteConfig by lazy {
    val firebaseApp = Firebase.app(firebaseAppName)
    Firebase.remoteConfig(firebaseApp)
}

val firebaseAnalytics by lazy {
    Firebase.analytics
}

fun initializeFirebase(
    context: Context,
    options: PromptimizerOptions
) {
    FirebaseApp.initializeApp(context)

    val firebaseOptions = FirebaseOptions.Builder()
        .setProjectId(options.firebaseConfig.projectId)
        .setApplicationId(options.firebaseConfig.applicationId)
        .setApiKey(options.firebaseConfig.apiKey)
        .build()
    Firebase.initialize(context, firebaseOptions, firebaseAppName)

    val configSettings = remoteConfigSettings {
        if (BuildConfig.DEBUG) {
            minimumFetchIntervalInSeconds = 10
        }
    }
    remoteConfig.setConfigSettingsAsync(configSettings)
    remoteConfig.fetchAndActivate()
}

fun trackFirebaseEvent(firebaseEvent: Event) {
    val bundle = Bundle().apply {
        firebaseEvent.parameters?.forEach { (key, value) ->
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
    firebaseAnalytics.logEvent(firebaseEvent.event, bundle)
}