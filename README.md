# Promptimizer - Intelligently ask for ratings
The Android client for [Promptimizer](https://get.asogiraffe.com/promptimizer)

![Promptimizer](https://user-images.githubusercontent.com/140911/116790809-2db52180-aa6b-11eb-9a15-19c45f350e9b.png)
_Get more ratings without annoying your users_

Promptimzer helps you ask your most loyal users to rate your app at just the right time.
Instead of spamming every user to rate your app, find the best location and time to ask users to rate.

## How does it work?
Pick screens in your app where you think your users are happiest.
For example, after they have successfully achieved a high score.

You can then show a sentiment prompt at these locations to a small percentage of your users to test which screen has the happiest users.
Once you find your most activated screens, you can show an in-app rating prompt to select users at that location.

Continue to experiment with more locations to find happier users.
You can even add conditions to prompt only after the user has triggered certain events.

## Requirements
Android minimum SDK 21+ (Android 5.0)

[Firebase for Android](https://firebase.google.com/docs/android/setup) with [Remote Config](https://firebase.google.com/docs/remote-config) and (optionally) [Analytics](https://firebase.google.com/docs/analytics)

## Install
```
repositories {
    maven { url "https://jitpack.io" }
}
dependencies {
    implementation 'com.github.promptimizer:promptimizer-android-sdk:0.0.2'
}
```

## Setup
Configure Promptimizer with your Firebase remote config and (optionally) your analytics.
```
Promptimizer.configure(firebaseRemoteConfig, firebaseAnalytics)
```

## Show a prompt
Set Promptimizer at ideal locations to display a sentiment and rating prompt
```
Promptimizer.getPrompt( “high_score_screen”) { prompt ->
  // You have full control if you want to show a prompt or not
  Promptimizer.showPrompt(activity, prompt)
}
```

## Configure a prompt
Lastly, open your Firebase console and [configure your sentiment and rating prompts](https://github.com/promptimizer/promptimizer-android-sdk/wiki/Firebase-Configuration) for Remote Config.

## Track events
It's often wise to trigger prompts once the user is "activated" within your app.
To do so, you can track your own analytics events, or you can use this convenience method to track events specific to prompts.
```
Promptimizer.track("high_score", mapOf("level" to 4, "score" to 870))
```

## Price
This library is 100% free to use. You will need to configure Firebase and manage your prompts and experiments. We are creating an add-on library that automatically configures prompts and experiments without the need to manage Firebase. We will offer a free and paid pricing tier. [Signup here](https://fedebehrens.typeform.com/to/j5SNfvc6) if you’d like to keep up to date.

## More questions?
Visit our FAQ and get in touch with us at [Promptimizer](https://get.asogiraffe.com/promptimizer).
