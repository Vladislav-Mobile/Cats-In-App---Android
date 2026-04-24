package com.example.catsinapp.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PetProfileDao {

    @Query("SELECT * FROM pet_profile WHERE id = 1")
    suspend fun getProfile(): PetProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProfile(profile: PetProfileEntity)
}
