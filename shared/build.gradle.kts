import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import com.varabyte.kobweb.gradle.library.util.configAsKobwebLibrary

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.buildkonfig)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.kobwebLibrary)
}

kotlin {
    configAsKobwebLibrary()

    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "11"
            }
        }
    }

    jvm("desktop")

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.serialization)
            implementation(libs.generativeai)
        }
    }
}

buildkonfig {
    packageName = "com.dangxuanthong.youtubeaisummarize"
    exposeObjectWithName = "BuildConfig"

    defaultConfigs {
        val localProperties = gradleLocalProperties(rootDir, providers)
        buildConfigField(STRING, "GENAI_KEY", localProperties.getProperty("GENAI_KEY"))
        buildConfigField(STRING, "YT_API_KEY", localProperties.getProperty("YT_API_KEY"))
    }
}

android {
    namespace = "com.dangxuanthong.youtubeaisummarize.shared"
    compileSdk = 34

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    defaultConfig {
        minSdk = 24
    }
    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}
