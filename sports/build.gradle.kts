import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("kotlin-parcelize")
}

val versionMajor = 1
val versionMinor = 0
val versionPatch = 0
/*
This is a build number base on date. This solution is better than using a build number base
on version number because play console need a build number greater each time a new apk is uploaded.
When using a build number based on version if your current beta apk is 1.3.0 and you want to publish
an update to your 1.2.0 production version you won't be able to do so. We can't use build number
based on milliseconds time though because of 2100000000 version code limitation. Here we make a
build number that increment only every minute so we should never reach 2100000000.
*/
val projectStartTimeMillis = 1517443200000
val versionBuild = ((System.currentTimeMillis() - projectStartTimeMillis) / 60000).toInt()

android {
    compileSdk = Versions.compileSdk
    buildToolsVersion = Versions.buildTools

    defaultConfig {
        applicationId = "com.komamj.wearable.sports"
        minSdk = Versions.minSdk
        targetSdk = Versions.targetSdk
        versionCode = versionBuild
        versionName = "$versionMajor.$versionMinor.$versionPatch"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        dataBinding = true
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    lint {
        isAbortOnError = true
        isIgnoreWarnings = true
        lintConfig = rootProject.file("lint.xml")
    }

    signingConfigs {
        create("release") {
            gradleLocalProperties(rootDir).let {
                keyAlias = it.getProperty("keyAlias")
                keyPassword = it.getProperty("keyPassword")
                storeFile = file(it.getProperty("storeFile"))
                storePassword = it.getProperty("storePassword")
            }
        }
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }

        getByName("release") {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
    }

    applicationVariants.all {
        outputs.all {
            // Since we're using date for versionCode, manifest will change each time we compile and
            // so we won't be able to use "Apply codee changes" features as it doesn't work when
            // manifest is modified. To avoid that we force a versionCode to 1 for debug build.
            val outputImpl = this as com.android.build.gradle.internal.api.ApkVariantOutputImpl
            if (name.contains("debug")) outputImpl.versionCodeOverride = 1
        }
    }
}

dependencies {
    dagger()

    implementation(project(":common"))
}
