package com.example.daytask.util

import android.content.Context
import coil.ImageLoader
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy

object ImageLoaderManager {
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