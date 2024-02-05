@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.kapt)
    id("kotlin-parcelize")
}

android {
    namespace = "com.citysavior.android.presentation"
    compileSdk = 34

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
}

dependencies {
    implementation(project(":domain"))

    implementation(libs.bundles.compose)
    implementation(libs.bundles.android.compose)
    implementation(libs.bundles.android.ktx)

    implementation(platform(libs.compose.bom))

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)

    debugImplementation(libs.bundles.android.compose.debug)
    //hlit
    implementation(libs.hilt.android)
    kapt(libs.dagger.hilt.compiler)

    //timber
    implementation(libs.timber)

    //google maps
    implementation(libs.google.map)
    implementation(libs.map.compose)

    //permission
    implementation(libs.accompanist.permission)
    implementation(libs.map.location)
}

kapt {
    correctErrorTypes = true
}