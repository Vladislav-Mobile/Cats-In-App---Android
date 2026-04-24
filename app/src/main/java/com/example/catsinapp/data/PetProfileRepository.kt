package com.example.catsinapp.data

import android.content.Context
import com.example.catsinapp.data.db.AppDatabase
import com.example.catsinapp.data.db.PetProfileEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class PetProfileData(
    val name: String      = "Buddy",
    val birthDate: String = "05/12/2021",
    val weightKg: String  = "12",
    val weightG: String   = "450",
    val photoUri: String  = ""
)

object PetProfileRepository {

    // Старые ключи SharedPreferences — нужны только для миграции
    private const val PREFS         = "pet_profile"
    private const val KEY_NAME      = "name"
    private const val KEY_DOB       = "dob"
    private const val KEY_WEIGHT_KG = "weight_kg"
    private const val KEY_WEIGHT_G  = "weight_g"
    private const val KEY_PHOTO     = "photo_uri"

    private val _profile = MutableStateFlow(PetProfileData())
    private val scope    = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    // ── Получить профиль ──────────────────────────────────────────────────────

    fun getProfile(context: Context): Flow<PetProfileData> {
        scope.launch {
            val dao     = AppDatabase.getInstance(context).petProfileDao()
            val entity  = dao.getProfile()

            if (entity != null) {
                // Room уже содержит данные
                _profile.value = entity.toData()
            } else {
                // Первый запуск — мигрируем из SharedPreferences в Room
                val migrated = loadFromPrefs(context)
                dao.saveProfile(migrated.toEntity())
                _profile.value = migrated
                // Очищаем SharedPreferences после миграции
                context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                    .edit().clear().apply()
            }
        }
        return _profile.asStateFlow()
    }

    // ── Сохранить профиль ─────────────────────────────────────────────────────

    fun saveProfile(context: Context, data: PetProfileData) {
        _profile.value = data   // StateFlow обновляется мгновенно — UI перерисовывается

        scope.launch {
            AppDatabase.getInstance(context).petProfileDao()
                .saveProfile(data.toEntity())   // виден в Database Inspector
        }
    }

    // ── Миграция из SharedPreferences ─────────────────────────────────────────

    private fun loadFromPrefs(context: Context): PetProfileData {
        val p = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        return PetProfileData(
            name      = p.getString(KEY_NAME,      "Buddy")      ?: "Buddy",
            birthDate = p.getString(KEY_DOB,       "05/12/2021") ?: "05/12/2021",
            weightKg  = p.getString(KEY_WEIGHT_KG, "12")         ?: "12",
            weightG   = p.getString(KEY_WEIGHT_G,  "450")        ?: "450",
            photoUri  = p.getString(KEY_PHOTO,     "")           ?: ""
        )
    }
}

// ── Конвертеры ────────────────────────────────────────────────────────────────

fun PetProfileEntity.toData() = PetProfileData(
    name      = name,
    birthDate = birthDate,
    weightKg  = weightKg,
    weightG   = weightG,
    photoUri  = photoUri
)

fun PetProfileData.toEntity() = PetProfileEntity(
    id        = 1,
    name      = name,
    birthDate = birthDate,
    weightKg  = weightKg,
    weightG   = weightG,
    photoUri  = photoUri
)
