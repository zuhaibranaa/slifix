# Slifix

An Android food-delivery app for the Bangladeshi market, in the mould of FoodPanda: browse
restaurants, build a cart and place an order, with map-based delivery tracking.

## Stack

| Layer | Choice |
|---|---|
| Platform | Android |
| Language | Java |
| Build | Gradle (`build.gradle`, `settings.gradle`, `gradle.properties`) |
| Package | `com.slifix.slifix` |

## Structure

```
app/src/main/java/com/slifix/slifix/
  MainActivity.java      entry point and main screen
  MapsActivity.java      delivery location and map view
app/src/main/AndroidManifest.xml
```

## Building

Open the project in Android Studio and run the `app` configuration, or build from the CLI with a
locally installed Android SDK:

```bash
./gradlew assembleDebug
```

The debug APK is written to `app/build/outputs/apk/debug/`.

## Google Maps key

The key is supplied at build time through a Gradle manifest placeholder, so it is never in the
repository. Put it in `local.properties` (git-ignored):

```properties
GOOGLE_MAPS_API_KEY=AIza...
```

`app/build.gradle` reads it with `project.findProperty('GOOGLE_MAPS_API_KEY')` and injects it as
the `googleMapsKey` placeholder used by the manifest.

## Notes

- Firebase configuration and other credentials are supplied at build time — they are not
  committed to the repository.
