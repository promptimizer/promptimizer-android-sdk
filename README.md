# Promptimizer - Intelligently ask for ratings
The Android client for [Promptimizer](https://get.asogiraffe.com/promptimizer)

![Promptimizer](https://user-images.githubusercontent.com/140911/116790809-2db52180-aa6b-11eb-9a15-19c45f350e9b.png)
_Get more ratings without annoying your users_

Promptimzer helps you ask your most loyal users to rate your app at just the right time.
Instead of spamming every user to rate your app, find the best location and time to ask users to rate.

## How does it work?
Pick two to three places in your app where you think your users are happiest.
For example, after they have successfully achieved a high score and are on the high score screen.

You can then show a sentiment prompt at this locations to a small percentage of your users to verify they are happy at this location.
Once one of these locatiosn is proven to have the happiest users, a small percentage of your users will now see an in-app rating prompt at that location.

Continue to add more locations to find happier users.
You can even add conditions to prompt users at a location only after the user has triggered certain events.

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
Set Promptimizer at ideal locations for the user to show the sentiment and rating prompt
```
Promptimizer.getPrompt( “high_score_screen”) { prompt ->
  // You have full control if you want to show a prompt or not
  Promptimizer.showPrompt(activity, prompt)
}
```

## Configure a prompt
Lastly, open your [Firebase console](https://console.firebase.google.com/) and configure your sentiment and rating prompts for Remote Config.
See our [Rating Recipes documentation](https://github.com/promptimizer/promptimizer-android-sdk/wiki) for best practices on configuring prompts.

## Track events
It's often wise to trigger prompts once the user is "activated" within your app.
You can track your own analytics events, or you can use the convenience method to track events specific to prompts.
```
Promptimizer.track("high_score", mapOf("level" to 4, "score" to 870)
```

## Price
This library is 100% free to use. You will need to configure Firebase and manage your prompts and experiments. We are creating an add-on library that automatically configures prompts and experiments without the need to manage Firebase. We will offer a free and paid pricing tier. [Signup here](https://fedebehrens.typeform.com/to/j5SNfvc6) if you’d like to keep up to date.

## More questions?
Visit our FAQ and get in touch with us at [Promptimizer](https://get.asogiraffe.com/promptimizer).
