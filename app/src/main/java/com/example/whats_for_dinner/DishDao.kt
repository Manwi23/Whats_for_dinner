package com.example.whats_for_dinner

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DishDao {

    @Query("SELECT * from dish_list ORDER BY name ASC")
    fun getAlphabetizedDishes(): LiveData<List<Dish>>

    @Query("SELECT * from dish_list ORDER BY name ASC")
    fun getAlphabetizedDishesDead(): List<Dish>

    @Query("SELECT * from dish_list WHERE type = :type ORDER BY name ASC")
    fun getAlphabetizedDishesByType(type: String): List<Dish>

    @Query("SELECT distinct type from dish_list ORDER BY type ASC")
    fun getAlphabetizedTypes(): List<String>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(dish: Dish)

    @Delete
    suspend fun delete(dish: Dish)

    @Query("DELETE FROM dish_list")
    suspend fun deleteAll()

}