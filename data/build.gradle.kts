plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
//    id("com.android.library")
//    id("kotlin-android")

    id("org.jetbrains.kotlinx.kover")
    alias(libs.plugins.kotlin.ksp)
}

/*java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}*/

android {
    namespace = "com.deny.data"
    compileSdk = Versions.ANDROID_COMPILE_SDK_VERSION

    defaultConfig {
        minSdk = Versions.ANDROID_MIN_SDK_VERSION

        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        getByName(BuildType.RELEASE) {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }

        getByName(BuildType.DEBUG) {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_18
        targetCompatibility = JavaVersion.VERSION_18
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_18.toString()
    }

    lintOptions {
        isCheckDependencies = true
        xmlReport = true
        xmlOutput = file("build/reports/lint/lint-result.xml")
    }
}

dependencies {
    implementation(project(Modules.DOMAIN))

    implementation(libs.androidx.core.ktx)
    implementation(libs.hilt.android)
    implementation(libs.moshi)
    implementation(libs.kotlin.stdlib)
    implementation(libs.javax.inject)

    implementation(libs.kotlinx.coroutines.android)

    api(libs.converter.moshi)
    api(libs.retrofit)

    api(libs.moshi.adapters)
    api(libs.moshi.kotlin)

    api(libs.okhttp)
    api(libs.logging.interceptor)

    api(libs.cloudinary.android)

    api(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Testing
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotest.assertions.core)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.androidx.core)
    testImplementation(libs.turbine)

}