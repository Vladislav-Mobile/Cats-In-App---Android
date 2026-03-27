package com.example.catsinapp.data

import com.example.catsinapp.R
import com.example.catsinapp.data.model.CareCategory
import com.example.catsinapp.data.model.FeedingLog
import com.example.catsinapp.data.model.Pet
import com.example.catsinapp.data.model.PetStatus

object DataSource {

    fun getPets(): List<Pet> {
        return listOf(
            Pet(
                id = "1",
                name = "Milo",
                status = PetStatus.STAR_OF_WEEK,
                imageRes = R.drawable.cat_milo,
                subtitle = "Playful and curious"
            ),
            Pet(
                id = "2",
                name = "Luna",
                status = PetStatus.NEW_ARRIVAL,
                imageRes = R.drawable.cat_luna,
                subtitle = "Calm and affectionate"
            ),
            Pet(
                id = "3",
                name = "Leo",
                status = PetStatus.ACTIVE,
                imageRes = R.drawable.cat_charlie,
                subtitle = "Energetic explorer"
            ),
            Pet(
                id = "4",
                name = "Bella",
                status = PetStatus.ACTIVE,
                imageRes = R.drawable.cat_bella,
                subtitle = "Sweet and friendly"
            ),
            Pet(
                id = "5",
                name = "Max",
                status = PetStatus.NEW_ARRIVAL,
                imageRes = R.drawable.cat_oliver,
                subtitle = "Loves attention"
            )
        )
    }

    fun getCareCategories(): List<CareCategory> {
        return listOf(
            CareCategory("1", "Feeding"),
            CareCategory("2", "Health"),
            CareCategory("3", "Grooming")
        )
    }

    fun getFeedingLogsState1(): List<FeedingLog> {
        return listOf(
            FeedingLog("1", "1", "Dry food"),
            FeedingLog("2", "1", "Wet food"),
            FeedingLog("3", "2", "Snacks")
        )
    }

    fun getFeedingLogsState2(): List<FeedingLog> {
        return listOf(
            FeedingLog("1", "1", "Dry food"),
            FeedingLog("2", "1", "Wet food"),
            FeedingLog("3", "2", "Snacks"),
            FeedingLog("4", "3", "Fish"),
            FeedingLog("5", "4", "Chicken"),
            FeedingLog("6", "5", "Milk")
        )
    }

    fun getFeedingLogsEmpty(): List<FeedingLog> {
        return emptyList()
    }

    fun getStarsOfTheWeek(): List<Pet> {
        return getPets().filter { it.status == PetStatus.STAR_OF_WEEK }
    }

    fun getNewArrivals(): List<Pet> {
        return getPets().filter { it.status == PetStatus.NEW_ARRIVAL }
    }

    fun getActiveCats(): List<Pet> {
        return getPets().filter { it.status == PetStatus.ACTIVE }
    }

    fun getPetById(id: String): Pet? {
        return getPets().firstOrNull { it.id == id }
    }
}