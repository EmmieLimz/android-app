plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services") // Apply the plugin
    id("com.google.devtools.ksp") // Apply KSP plugin
}

android {
    namespace = "com.nipa.healthcaremobile"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.nipa.healthcaremobile"
        minSdk = 28 // Android 9.0 (Pie) as per NFR5
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    // Required for view binding or data binding if used later, good to have basic setup
    buildFeatures {
        viewBinding = true
        // dataBinding = true
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1") // Latest as of late 2023/early 2024
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // Lifecycle components (for ViewModels)
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.2")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.2")
    implementation("androidx.activity:activity-ktx:1.9.0") // For by viewModels()

    // Room for local database
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    implementation("androidx.navigation:navigation-fragment-ktx:2.9.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.9.0") // For Coroutines and Flow support
    ksp("androidx.room:room-compiler:2.6.1") // Room compiler using KSP

    // WorkManager for background sync (as per requirements)
    implementation("androidx.work:work-runtime-ktx:2.9.0")

    // Coroutines support for Firebase
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3") // Or latest

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.15.0"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-storage")
    implementation("com.google.firebase:firebase-messaging")
    implementation("com.google.firebase:firebase-analytics")

    // Testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Mockito for testing (optional, but good practice)
    testImplementation("org.mockito:mockito-core:5.7.0")
    // testImplementation("org.mockito.kotlin:mockito-kotlin:5.2.1") // If using mockito-kotlin
}
