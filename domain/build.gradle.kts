plugins {
    id(libs.plugins.java.library.get().pluginId)
    id("kotlin")
    alias(libs.plugins.kotlinx.kover)
//    alias(libs.plugins.kotlin.ksp)
}

java {
    sourceCompatibility = JavaVersion.VERSION_18
    targetCompatibility = JavaVersion.VERSION_18
}

dependencies {

    implementation(libs.javax.inject)
    implementation(libs.kotlinx.coroutines.android)

    api(libs.androidx.room.common)
//    ksp(libs.androidx.room.compiler)

    // Testing
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotest.assertions.core)
    testImplementation(libs.kotlinx.coroutines.test)

}