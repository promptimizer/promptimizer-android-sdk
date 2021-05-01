# Promptimizer - Intelligently ask for ratings
The Android client for [Promptimizer](https://get.asogiraffe.com/promptimizer)

![Promptimizer](https://user-images.githubusercontent.com/140911/116790809-2db52180-aa6b-11eb-9a15-19c45f350e9b.png)
_Get more ratings without annoying your users_

Promptimzer helps you ask your most loyal users to rate your app at just the right time. Instead of spamming every user to rate your app, find the best location and time to ask users to rate.

## How does it work?
Pick two to three places in your app where you think your users are happiest. For example, after they have successfully purchased an item from the store. You can then show a sentiment prompt at these locations to a small percentage of your users to verify they are happy at this location.

Once the location is proven to have the happiest users, a small percentage of your users will see an in-app rating prompt at that location.

Continue to add more locations to find happier users. You can even add conditions to prompt users at a location only after thresholds or certain events have occurred.

## Install
```
repositories {
    maven { url "https://jitpack.io" }
}
dependencies {
    implementation 'com.github.promptimizer:promptimizer-android-sdk:0.0.1'
}
```

## Setup
```
Promptimizer.configure(contex, firebaseConfig)
```

Set the Promptimizer at ideal locations for the user, and show the sentiment or rating prompt
```
Promptimizer.getPrompt( “high_score_screen”) { prompt ->
  // You have full control if you want to show a prompt or not
  Promptimizer.showPrompt(activity, prompt)
}
```

Lastly, open your Firebase console and configure your sentiment and rating prompts. Optionally, add conditions or event thresholds. See our Rating Recipes documentation for best practices (coming soon!).

## Price
This library is 100% free to use. You will need to configure Firebase and manage your prompts and experiments. We are creating an add-on library that automatically manages prompts/experiments without the need to manage Firebase. We will offer a free and paid pricing tier. [Signup here](https://fedebehrens.typeform.com/to/j5SNfvc6) if you’d like to keep updated.

## More questions?
Visit our FAQ and get in touch with us at [Promptimizer](https://get.asogiraffe.com/promptimizer).
