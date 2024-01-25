@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.kapt)
    id("com.google.gms.google-services")
    id("com.google.android.gms.oss-licenses-plugin")
}

android {
    namespace = "com.citysavior.android"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.citysavior.android"
        targetSdk = 34
        minSdk = 26
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
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
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.bundles.compose)
    implementation(libs.bundles.android.compose)
    implementation(libs.bundles.android.ktx)
    implementation(platform(libs.compose.bom))
    debugImplementation(libs.bundles.android.compose.debug)

    //hlit
    implementation(libs.hilt.android)
    kapt(libs.dagger.hilt.compiler)

    implementation(libs.bundles.network)
    implementation(platform(libs.okthhp.bom))

    implementation(libs.splashscreen)

    //datastore
    implementation(libs.datastore.preferences)
    //firebase
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
    implementation("com.google.firebase:firebase-analytics")

    //oss-licenses
    implementation ("com.google.android.gms:play-services-oss-licenses:17.0.0")

    //room
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    annotationProcessor(libs.room.compiler)

    //timber
    implementation(libs.timber)

    implementation(project(":presentation"))
    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(project(":core"))
}

kapt {
    correctErrorTypes = true
}