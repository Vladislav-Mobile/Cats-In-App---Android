package com.example.catsinapp.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pet_profile")
data class PetProfileEntity(
    @PrimaryKey val id: Int = 1,   // всегда одна строка
    val name: String,
    val birthDate: String,
    val weightKg: String,
    val weightG: String,
    val photoUri: String
)
