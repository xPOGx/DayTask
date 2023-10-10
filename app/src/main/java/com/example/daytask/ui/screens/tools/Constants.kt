package com.example.daytask.ui.screens.tools

import android.content.Context
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.SecureFlagPolicy
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy

object Constants {
    const val PASSWORD_LENGTH = 6
    const val NAME_LENGTH = 3
    const val WEB_CLIENT_ID = "1097689850055-vtg3q678f856r7815kgmmofb704rd640.apps.googleusercontent.com"
    val noDismissProperties = DialogProperties(
        dismissOnBackPress = false,
        dismissOnClickOutside = false,
        securePolicy = SecureFlagPolicy.SecureOn
    )
    @Volatile
    private var imageLoader: ImageLoader? = null
    fun getImageLoader(context: Context): ImageLoader =
        imageLoader ?: synchronized(this) {
            ImageLoader.Builder(context)
                .diskCache {
                    DiskCache.Builder()
                        .directory(context.cacheDir.resolve("image_cache"))
                        .maxSizePercent(0.02)
                        .build()
                }
                .diskCachePolicy(CachePolicy.ENABLED)
                .memoryCache {
                    MemoryCache.Builder(context)
                        .maxSizePercent(0.25)
                        .build()
                }
                .memoryCachePolicy(CachePolicy.ENABLED)
                .respectCacheHeaders(false)
                .build()
                .also { imageLoader = it }
        }
}