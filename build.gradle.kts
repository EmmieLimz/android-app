// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    // Define plugin versions here. These should match or be compatible with your Android Studio version
    // and project requirements. Using common recent versions as of early 2024.
    id("com.android.application") version "8.10.1" apply false // Or your AGP version
    id("org.jetbrains.kotlin.android") version "2.0.0" apply false // Or your Kotlin version
    id("com.google.gms.google-services") version "4.4.2" apply false // For Firebase
    id("com.google.devtools.ksp") version "2.0.0-1.0.21" apply false // For KSP
}

// Task to clean the build directory.
tasks.register("clean", Delete::class) {
    delete(layout.buildDirectory) // Use layout.buildDirectory
}
