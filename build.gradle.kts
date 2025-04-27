plugins {
    id("com.android.application") version "8.7.3" apply false
    id("com.android.library") version "8.7.3" apply false
    id("org.jetbrains.kotlin.android") version "2.1.0" apply false
}

buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    dependencies {
        classpath(libs.gradle.v822)
        classpath(libs.google.services.v4315) // Plugin Firebase
        classpath(libs.kotlin.gradle.plugin.v1910)
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}
