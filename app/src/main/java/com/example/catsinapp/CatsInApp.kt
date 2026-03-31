package com.example.catsinapp

import android.app.Application
import coil.Coil
import coil.ImageLoader
import coil.decode.SvgDecoder


class CatsInApp : Application() {
    override fun onCreate() {
        super.onCreate()
        // Настраиваем Coil с поддержкой SVG
        // Нужно для отображения GitHub profile views counter
        Coil.setImageLoader(
            ImageLoader.Builder(this)
                .components {
                    add(SvgDecoder.Factory())
                }
                .respectCacheHeaders(false)  // кешируем счётчик
                .build()
        )
    }
}