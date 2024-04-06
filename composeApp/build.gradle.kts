import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.buildkonfig)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        moduleName = "composeApp"
        browser {
            commonWebpackConfig {
                outputFileName = "composeApp.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(project.projectDir.path)
                    }
                }
            }
        }
        binaries.executable()
    }

    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "11"
            }
        }
    }

    jvm("desktop")

    sourceSets {

        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
            implementation(libs.koin.android)
            implementation(libs.ktor.android)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.bundles.koin)
            implementation(libs.bundles.precompose)
            implementation(libs.generativeai)
            implementation(libs.bundles.ktor)
        }
        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(libs.ktor.okhttp)
                //noinspection UseTomlInstead
                implementation("org.slf4j:slf4j-simple:2.0.12")
            }
        }
        val wasmJsMain by getting {
            dependencies {
                implementation(libs.ktor.js)
            }
        }
    }
}

buildkonfig {
    packageName = "com.dangxuanthong.youtube_video_summarize"
    objectName = "BuildConfig"

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
    namespace = "com.dangxuanthong.youtube_video_summarize"
    compileSdk = 34

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "com.dangxuanthong.youtube_video_summarize"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        release {
            isMinifyEnabled = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    dependencies {
        debugImplementation(libs.compose.ui.tooling)
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.dangxuanthong.youtube_video_summarize"
            packageVersion = "1.0.0"
        }
    }
}

compose.experimental {
    web.application {}
}