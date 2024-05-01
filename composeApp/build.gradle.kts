@file:OptIn(ExperimentalKotlinGradlePluginApi::class)

import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.ksp)

    alias(libs.plugins.androidApplication)
    alias(libs.plugins.hydraulicConveyor)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget = JvmTarget.JVM_17
        }
    }

    jvm("desktop")
    jvmToolchain(17)

    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
            implementation(libs.koin.android)
        }
        commonMain.dependencies {
            implementation(projects.shared)
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.landscapist.coil)
            implementation(libs.bundles.precompose)

            implementation(project.dependencies.platform(libs.ktor.bom))
            implementation(libs.bundles.ktor)

            implementation(libs.generativeai)

            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.bundles.koin)
        }
        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(libs.slf4j.simple)
            }
        }
    }
}

dependencies {
    add("kspCommonMainMetadata", libs.koin.ksp.compiler)
    add("kspAndroid", libs.koin.ksp.compiler)
    add("kspDesktop", libs.koin.ksp.compiler)

    // Use the configurations created by the Conveyor plugin
    // to tell Gradle/Conveyor where to find the artifacts for each platform.
    // **Will support macOS in the future**
    windowsAmd64(compose.desktop.windows_x64)
//    macAmd64(compose.desktop.macos_x64)
//    macAarch64(compose.desktop.macos_arm64)
}

android {
    namespace = "com.dangxuanthong.youtubeaisummarize"
    compileSdk = 34

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "com.dangxuanthong.youtubeaisummarize"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0preview1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
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
    dependencies {
        debugImplementation(libs.compose.ui.tooling)
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
    }
}

// Needed by Conveyor to export desktop apps
group = "com.dangxuanthong"
version = "1.0preview1"

// region Work around temporary Compose bugs.
configurations.all {
    attributes {
        // https://github.com/JetBrains/compose-jb/issues/1404#issuecomment-1146894731
        attribute(Attribute.of("ui", String::class.java), "awt")
    }
}
// endregion
