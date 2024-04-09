import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import com.varabyte.kobweb.gradle.library.util.configAsKobwebLibrary
import java.util.Properties

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
        }
    }
}

buildkonfig {
    packageName = "com.dangxuanthong.youtube_video_summarize"
    exposeObjectWithName = "BuildConfig"

    defaultConfigs {
        buildConfigField(STRING, "GENAI_KEY", System.getenv("GENAI_KEY") ?: "")
    }

    defaultConfigs("dev") {
        val properties = Properties().apply {
            val file = project.rootProject.file("local.properties")
            if (file.exists()) load(file.reader())
        }
        buildConfigField(STRING, "GENAI_KEY", properties.getProperty("GENAI_KEY") ?: "")
    }
}

android {
    namespace = "com.dangxuanthong.youtube_video_summarize.shared"
    compileSdk = 34

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    defaultConfig {
        minSdk = 24
    }
}