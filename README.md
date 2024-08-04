# Video Recording and Upload Application

Hello! We hope you are doing great today and would like to thank you for taking your time to work on this exercise.

## Project Overview

![Demo](assets/demo.gif)

### Objective

Build an Android application that allows users to record videos and upload them to a specified REST API endpoint with the following features:

1. **Start and Stop Video Recording**: A button to start and stop video recording using the device's camera.
2. **Upload Video**: A button to upload the recorded video to a specified REST API endpoint.
   - Example using Cloudinary:

      ```
      URL: https://api.cloudinary.com/v1_1/dk3lhojel/video/upload
      Request Payload:
      form-data
      file: video-file.mp4
      upload_preset: android_sample
      public_id: video-file.mp4
      api_key: xxxxxx
      ```
3. **Save to Local Database**: Save the video information to a local database after a successful upload.
4. **Show List of Uploaded Videos**: Display a list of uploaded videos from the local database.
5. **Play Video**: Open and play the video from the list of uploaded videos.


### Technologies and Libraries

- **Language**: Kotlin
- **Architecture**: MVVM
- **Dependency Injection**: Dagger Hilt
- **Asynchronous Programming**: Kotlin Coroutines, Flow
- **Androidx.camera**: Camera
- **Network**: Retrofit, OkHttp, Moshi
- **Image Loading**: Coil
- **Logging**: Timber, Chucker
- **UI Framework**: Jetpack Compose
- **Local Database**: Room
- **Video Player**: ExoPlayer
- **Cloudinary SDK**: Player

## Setup

- Clone the project
- Open the project with Android Studio
- Sync and build the project

## Linter and Static Code Analysis

- **Lint**:

  ```bash
  $ ./gradlew lint
  ```

  Report is located at: `./app/build/reports/lint/`

- **Detekt**:

  ```bash
  $ ./gradlew detekt
  ```

  Report is located at: `./build/reports/detekt`

## Run the App

- Deploy the app to an emulator or physical device.
