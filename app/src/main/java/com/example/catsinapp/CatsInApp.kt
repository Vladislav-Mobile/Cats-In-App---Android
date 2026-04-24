package com.example.catsinapp

import android.app.Application
import coil.Coil
import coil.ImageLoader
import coil.decode.SvgDecoder
import com.example.catsinapp.data.db.AppDatabase


class CatsInApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // Инициализируем Room при старте — виден в Database Inspector
        AppDatabase.getInstance(this)

        // Настраиваем Coil с поддержкой SVG
        Coil.setImageLoader(
            ImageLoader.Builder(this)
                .components {
                    add(SvgDecoder.Factory())
                }
                .respectCacheHeaders(false)
                .build()
        )
    }
}