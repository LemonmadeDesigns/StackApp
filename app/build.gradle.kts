plugins {
    // Both application and kotlin plugins must be active
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.example.stackapp"
    compileSdk = 35 // Using 35 as it's the stable standard

    defaultConfig {
        applicationId = "com.example.stackapp"
        minSdk = 24
        targetSdk = 35 // Match compileSdk
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

    // Set the Java version for compilation
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    // Minimal Android dependencies - only what we actually use
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)

    // Testing dependencies (optional, can be removed if not testing)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

// Auto-commit task: Runs after successful builds
tasks.register<Exec>("autoCommit") {
    group = "git"
    description = "Auto-commit changes after successful build"
    workingDir = projectDir.parentFile
    commandLine("sh", "auto-commit.sh")
}

// Auto-commit after successful builds (Semi-Automatic Mode)
// This runs automatically after assembleDebug succeeds
afterEvaluate {
    tasks.named("assembleDebug").configure {
        finalizedBy("autoCommit")
    }
}