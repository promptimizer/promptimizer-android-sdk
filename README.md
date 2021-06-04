# Promptimizer - Intelligently ask for ratings
The Android client for [Promptimizer](https://get.asogiraffe.com/promptimizer)

![Promptimizer](https://user-images.githubusercontent.com/140911/116790809-2db52180-aa6b-11eb-9a15-19c45f350e9b.png)
_Get more ratings without annoying your users_

Don't spam every user to rate your app.
Promptimzer helps you ask your most loyal users to rate your app at just the right time.

## How does it work?
Pick a few places in your app where you think your users are happiest.
For example, after they have successfully achieved a high score.

A small percentage of users will see a "Do you like this app?" prompt at these locations.
This will help you find your most activated, happiest users.
Once the ideal location is determined, a small percentage of users will see an in-app review.

You can further experiment and optimize the prompts to only trigger after certain events.
We even have some [rating recipes](https://github.com/promptimizer/promptimizer-android-sdk/wiki) you can try.

## Requirements
Android minimum SDK 21+ (Android 5.0).

[Firebase for Android](https://firebase.google.com/docs/android/setup) with [Remote Config](https://firebase.google.com/docs/remote-config) and (optionally) [Analytics](https://firebase.google.com/docs/analytics).

## Install
```
repositories {
    // Note: moving to Maven Central in the future
    maven { url "https://jitpack.io" }
}
dependencies {
    implementation 'com.github.promptimizer:promptimizer-android-sdk:<latest-version>'
}
```
See the [latest release versions](https://github.com/promptimizer/promptimizer-android-sdk/releases).

## Setup
Pass in your Firebase remote config and (optionally) your analytics.
```
Promptimizer.configure(firebaseRemoteConfig, firebaseAnalytics)
```

## Show a prompt
Pick ideal locations to display a sentiment or rating prompt.
```
Promptimizer.getPrompt( “high_score_screen”) { prompt ->
  // You have full control over displaying a prompt or not
  Promptimizer.showPrompt(activity, prompt)
}
```

## Configure a prompt
Lastly, open your Firebase console and [configure your sentiment and rating prompts](https://github.com/promptimizer/promptimizer-android-sdk/wiki) for Remote Config.

## Track events
It's wise to trigger prompts only after the user has performed certain actions.
You can target prompts to analytics events you already track, or use this convenience method to send events specific to prompts.
```
Promptimizer.track("high_score", mapOf("level" to 4, "score" to 870))
```

## Price
The library is 100% free to use. You will need to manage your Firebase though. We are developing an add-on library that automatically configures prompts and experiments. This add-on will have a free option and paid pricing tier. [Signup here](https://fedebehrens.typeform.com/to/j5SNfvc6) if you’d like to keep in the loop.

## More questions? Feedback?
Visit [Promptimizer](https://get.asogiraffe.com/promptimizer) or open a [discussion](https://github.com/promptimizer/promptimizer-android-sdk/discussions).
