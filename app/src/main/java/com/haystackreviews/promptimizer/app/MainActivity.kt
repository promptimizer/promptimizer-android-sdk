package com.haystackreviews.promptimizer.app

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.promptimizer.Error
import com.promptimizer.Prompt
import com.promptimizer.Promptimizer
import com.haystackreviews.promptimizer.app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val firebaseRemoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 10
        }
        firebaseRemoteConfig.setConfigSettingsAsync(configSettings)
        firebaseRemoteConfig.fetchAndActivate()

        val firebaseAnalytics = Firebase.analytics

        binding.initializeSdkButton.setOnClickListener {
            Promptimizer.configure(
                firebaseRemoteConfig,
                firebaseAnalytics
            ) { error ->
                if (error != null) {
                    Toast.makeText(this, error.message, Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "SDK Configuration complete", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }
        binding.locationOneEventButton.setOnClickListener {
            Promptimizer.getPrompt(
                "location-one",
            ) { error, prompt ->
                handlePrompt(error, prompt)

            }
        }
        binding.locationTwoEventButton.setOnClickListener {
            Promptimizer.getPrompt(
                "location-two"
            ) { error, prompt ->
                handlePrompt(error, prompt)
            }
        }
        binding.trackEvent.setOnClickListener {
            val eventName = binding.eventName.text.toString()
            val eventKey = binding.eventKey.text.toString()
            val eventValue = binding.eventValue.text.toString()

            if (eventName.isEmpty()) {
                Toast.makeText(this, "Event name is empty", Toast.LENGTH_SHORT).show()
            } else {
                val params = if (eventKey.isNotEmpty()) {
                    mapOf(Pair(eventKey, eventValue))
                } else {
                    emptyMap()
                }
                Toast.makeText(
                    this,
                    "Sending event $eventName and params $params",
                    Toast.LENGTH_SHORT
                ).show()

                Promptimizer.track(
                    binding.eventName.text.toString(),
                    params
                ) { error ->
                    error?.let {
                        Toast.makeText(
                            this,
                            it.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
    }

    private fun handlePrompt(error: Error?, prompt: Prompt?) {
        when {
            error != null -> {
                Toast.makeText(this, error.message, Toast.LENGTH_LONG).show()
            }
            prompt != null -> {
                Toast.makeText(this, "Preparing Prompt $prompt", Toast.LENGTH_LONG).show()
                Promptimizer.showPrompt(this, prompt) { error, prompt ->
                    if (error == null) {
                        Toast.makeText(this, "Prompt flow completed", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, error.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else -> {
                Toast.makeText(this, "No prompt to display", Toast.LENGTH_SHORT).show()
            }
        }
    }
}