import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.codingfeline.buildkonfig.compiler.FieldSpec
import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import com.codingfeline.buildkonfig.gradle.TargetConfigDsl
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.buildkonfig)

    alias(libs.plugins.androidApplication)
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

            implementation(project.dependencies.platform(libs.ktor.bom))
            implementation(libs.bundles.ktor)

            implementation(libs.bundles.precompose)

            implementation(libs.generativeai)

            implementation(project.dependencies.platform(libs.koin.bom))
            implementation(libs.bundles.koin)
        }
        val desktopMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(libs.ktor.okhttp)
                implementation(libs.slf4j.simple)
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
    packageName = "com.dangxuanthong.youtube_ai_summarize"
    objectName = "BuildConfig"

    val localProperties = gradleLocalProperties(rootDir, providers)

    fun TargetConfigDsl.buildConfigField(
        type: FieldSpec.Type,
        name: String,
        key: CharSequence
    ) {
        val value = localProperties.getProperty(key.toString()) ?: System.getenv(key.toString())
        val defaultValue = when (type) {
            STRING -> ""
            FieldSpec.Type.INT -> "0"
            FieldSpec.Type.FLOAT -> "0"
            FieldSpec.Type.LONG -> "0"
            FieldSpec.Type.BOOLEAN -> "false"
        }
        buildConfigField(type, name, value ?: defaultValue)
    }

    defaultConfigs {
        buildConfigField(STRING, "GENAI_KEY", key = "GENAI_KEY")
    }
}

android {
    namespace = "com.dangxuanthong.youtube_ai_summarize"
    compileSdk = 34

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "com.dangxuanthong.youtube_ai_summarize"
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
            packageName = "com.dangxuanthong.youtube_ai_summarize"
            packageVersion = "1.0.0"
        }
    }
}

compose.experimental {
    web.application {}
}