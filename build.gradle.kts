// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:${Versions.android_build_tools}")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.31")
        classpath("com.google.dagger:hilt-android-gradle-plugin:${Versions.hilt}")
    }
}

plugins {
    id("com.diffplug.spotless") version Versions.spotless apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
        jcenter()
    }
}

subprojects {
    apply(plugin = "com.diffplug.spotless")
    configure<com.diffplug.gradle.spotless.SpotlessExtension> {
        kotlin {
            target("**/*.kt")
            ktlint(Versions.ktlint).userData(
                    mapOf(
                            "disabled_rules" to "no-wildcard-imports",
                            "max_line_length" to "120"
                    )
            )

            licenseHeaderFile(file("${rootDir.absolutePath}/copyright.kt"))
        }
        kotlinGradle {
            target("**/*.gradle.kts")
            ktlint(Versions.ktlint)
        }
    }
    tasks.withType < org.jetbrains.kotlin.gradle.tasks.KotlinCompile > ().configureEach {
        kotlinOptions {
            allWarningsAsErrors = false
        }
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}