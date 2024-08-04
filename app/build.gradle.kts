plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlinx.kover)
//    id("kotlin-kapt")
//    kotlin("kapt")
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.ksp)
    alias(libs.plugins.compose.compiler)
//    id("kotlin-kapt")
}

val keystoreProperties = rootDir.loadGradleProperties("signing.properties")

android {
    namespace = "com.deny.recordsend"
    compileSdk = Versions.ANDROID_COMPILE_SDK_VERSION

    defaultConfig {
        applicationId = "com.deny.recordsend"
        minSdk = Versions.ANDROID_MIN_SDK_VERSION
        targetSdk = Versions.ANDROID_COMPILE_SDK_VERSION
        versionCode = Versions.ANDROID_VERSION_CODE
        versionName = Versions.ANDROID_VERSION_NAME

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        getByName(BuildType.RELEASE) {
            isMinifyEnabled = true
            isDebuggable = false
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
//            signingConfig = signingConfigs[BuildType.RELEASE]
            buildConfigField("String", "BASE_API_URL", "\"https://api.cloudinary.com/v1_1/dfzx5ovbg/\"")
            buildConfigField("String", "CLOUD_NAME", "\"dfzx5ovbg\"")
        }
        getByName(BuildType.DEBUG) {
            // For quickly testing build with proguard, enable this
            isMinifyEnabled = false
//            signingConfig = signingConfigs[BuildType.DEBUG]
            buildConfigField("String", "BASE_API_URL", "\"https://api.cloudinary.com/v1_1/dfzx5ovbg/\"")
            buildConfigField("String", "CLOUD_NAME", "\"dfzx5ovbg\"")
        }
    }
    sourceSets["test"].resources {
        srcDir("src/test/resources")
    }
//    sourceSets["main"].resources {
//        srcDir("build/generated/ksp/main/kotlin")
//    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_18
        targetCompatibility = JavaVersion.VERSION_18
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_18.toString()
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    packaging {
        jniLibs {
            // Resolve "libmockkjvmtiagent.so" https://github.com/mockk/mockk/issues/297#issuecomment-901924678
            useLegacyPackaging = true
        }
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
        resources.excludes.add("META-INF/*")
    }
    lint {
        checkDependencies = true
        xmlReport = true
        xmlOutput = file("build/reports/lint/lint-result.xml")
    }

    testOptions {
        unitTests {
            // Robolectric resource processing/loading https://github.com/robolectric/robolectric/pull/4736
            isIncludeAndroidResources = true
        }
        // Disable device's animation for instrument testing
        // animationsDisabled = true
    }
    hilt {
        enableAggregatingTask = true
    }
}

//kapt {
//    correctErrorTypes = true
//}

dependencies {

    implementation(project(Modules.DATA))
    implementation(project(Modules.DOMAIN))

    implementation(libs.androidx.core.ktx)

    implementation(libs.hilt.android)
    implementation(libs.androidx.media3.ui)
    implementation(libs.androidx.material3.android)
    ksp(libs.hilt.compiler)

    implementation(libs.lifecycle.runtime)
    implementation(libs.lifecycle.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.tooling)
    implementation(libs.androidx.foundation)
    implementation(libs.androidx.material)
    implementation(libs.coil)
    implementation(libs.coil.video)

    implementation(libs.androidx.navigation.compose)
    implementation(libs.accompanist.systemuicontroller)

    implementation(libs.androidx.hilt.navigation.compose)

    implementation(libs.timber)

    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlinx.coroutines.android)

    debugImplementation(libs.library)
    releaseImplementation(libs.library.no.op)

    // Unit test
    testImplementation(libs.kotest.assertions.core)
    testImplementation(libs.mockk)
    testImplementation(libs.junit)
    testImplementation(libs.turbine)

    // UI test with Robolectric
    testImplementation(platform(libs.androidx.compose.bom))
    testImplementation(libs.androidx.ui.test.junit4)
    testImplementation(libs.androidx.rules)

    // UI test
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    androidTestImplementation(libs.androidx.rules)
    androidTestImplementation(libs.mockk.android)

    //Camera
    implementation(libs.androidx.camera.view)
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.video)

    implementation(libs.cloudinary.android)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.room)
    ksp(libs.androidx.room.compiler)


    implementation(libs.androidx.media3.exoplayer)
    implementation(libs.androidx.media3.ui)


}