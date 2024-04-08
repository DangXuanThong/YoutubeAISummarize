rootProject.name = "YoutubeVideoSummarize"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
//        maven(url = "https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

include(":composeApp")
include(":shared")