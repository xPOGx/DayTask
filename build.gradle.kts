// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    extra.apply {
        set("nav_version", "2.7.5")
        set("room_version", "2.6.0")
        set("lifecycle_version", "2.6.2")
        set("compose_bom_version", "2023.09.00")
        set("firebase_bom_version", "32.5.0")
    }
}

plugins {
    id("com.android.application") version "8.1.4" apply false
    id("org.jetbrains.kotlin.android") version "1.9.10" apply false
    // Firebase
    id("com.google.gms.google-services") version "4.3.15" apply false
}