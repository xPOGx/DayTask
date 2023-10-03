// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    extra.apply {
        set("nav_version", "2.6.0")
        set("room_version", "2.5.2")
        set("lifecycle_version", "2.6.2")
        set("bom_version", "2023.06.01")
    }
}

plugins {
    id("com.android.application") version "8.1.1" apply false
    id("org.jetbrains.kotlin.android") version "1.9.10" apply false
    // Firebase
    id("com.google.gms.google-services") version "4.3.15" apply false
}