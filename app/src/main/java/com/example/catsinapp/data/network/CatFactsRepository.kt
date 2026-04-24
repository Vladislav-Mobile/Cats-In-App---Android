package com.example.catsinapp.data.network

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Request
import org.json.JSONObject

object CatFactsRepository {

    private const val BASE_URL = "https://catfact.ninja/fact"

    /**
     * Запрашивает случайный факт о кошках.
     * Запрос виден в Android Studio → App Inspection → Network Inspector.
     * Возвращает строку с фактом или null при ошибке.
     */
    suspend fun fetchFact(): String? = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder().url(BASE_URL).build()
            val response = NetworkClient.okHttpClient.newCall(request).execute()
            if (response.isSuccessful) {
                val body = response.body?.string() ?: return@withContext null
                JSONObject(body).optString("fact", null)
            } else null
        } catch (e: Exception) {
            null
        }
    }
}
