// settings.gradle.kts

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal() // Standard repository for Gradle plugins
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS) // Recommended practice
    repositories {
        google()
        mavenCentral()
        // You can add other repositories here if needed, e.g., JitPack
        // maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "HealthcareApp" // Sets the project name
include(":app") // Includes the 'app' module in the build
