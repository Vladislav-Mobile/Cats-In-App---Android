package com.example.catsinapp.data

import android.content.Context
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

data class PetProfileData(
    val name: String      = "Buddy",
    val birthDate: String = "05/12/2021",
    val weightKg: String  = "12",
    val weightG: String   = "450",
    val photoUri: String  = ""
)

object PetProfileRepository {

    private const val PREFS         = "pet_profile"
    private const val KEY_NAME      = "name"
    private const val KEY_DOB       = "dob"
    private const val KEY_WEIGHT_KG = "weight_kg"
    private const val KEY_WEIGHT_G  = "weight_g"
    private const val KEY_PHOTO     = "photo_uri"

    private val _profile = MutableStateFlow(PetProfileData())

    fun getProfile(context: Context): Flow<PetProfileData> {
        // Загружаем из SharedPreferences при первом вызове
        val p = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        _profile.value = PetProfileData(
            name      = p.getString(KEY_NAME,      "Buddy")      ?: "Buddy",
            birthDate = p.getString(KEY_DOB,       "05/12/2021") ?: "05/12/2021",
            weightKg  = p.getString(KEY_WEIGHT_KG, "12")         ?: "12",
            weightG   = p.getString(KEY_WEIGHT_G,  "450")        ?: "450",
            photoUri  = p.getString(KEY_PHOTO,     "")           ?: ""
        )
        return _profile.asStateFlow()
    }

    fun saveProfile(context: Context, data: PetProfileData) {
        context.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit()
            .putString(KEY_NAME,      data.name)
            .putString(KEY_DOB,       data.birthDate)
            .putString(KEY_WEIGHT_KG, data.weightKg)
            .putString(KEY_WEIGHT_G,  data.weightG)
            .putString(KEY_PHOTO,     data.photoUri)
            .apply()
        _profile.value = data
    }
}