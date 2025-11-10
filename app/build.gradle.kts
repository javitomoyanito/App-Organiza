// Importa la clase Properties
import java.util.Properties

plugins {
    id("com.android.application") // <-- Paréntesis y comillas dobles
    id("org.jetbrains.kotlin.android") // <-- Paréntesis y comillas dobles
}

// Lógica de properties en Kotlin, movida al inicio
val properties = Properties()
val localPropertiesFile = project.rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    properties.load(localPropertiesFile.inputStream()) // <-- .inputStream()
}

android {
    namespace = "com.example.cameraapp" // <-- Signo =
    compileSdk = 34 // <-- Signo =

    defaultConfig {
        applicationId = "com.example.cameraapp" // <-- Signo =
        minSdk = 24 // <-- Signo =
        targetSdk = 35 // <-- Signo =
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true // <-- Signo =

        // Sintaxis de función con paréntesis
        buildConfigField("String", "GEMINI_API_KEY", "\"${properties.getProperty("GEMINI_API_KEY")}\"")
    }

    buildTypes {
        // Para configurar un tipo existente como 'release', usamos getByName
        getByName("release") {
            isMinifyEnabled = false // <-- 'is' prefijo y signo =
            buildConfigField("String", "GEMINI_API_KEY", "\"${properties.getProperty("GEMINI_API_KEY")}\"")
        }
    }

    buildFeatures {
        viewBinding = true // <-- Signo =
        buildConfig = true // <-- Signo =
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8 // <-- Signo =
        targetCompatibility = JavaVersion.VERSION_1_8 // <-- Signo =
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    // Todas las dependencias usan paréntesis y comillas dobles
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.activity:activity-ktx:1.9.0")
    implementation("com.google.firebase:firebase-vertexai:16.0.0-beta01")
    implementation("com.google.ai.client.generativeai:generativeai:0.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.3")
    implementation("androidx.activity:activity:1.9.0")
}