package com.example.whats_for_dinner

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface DishDao {

    @Query("SELECT * from dish_list ORDER BY name ASC")
    fun getAlphabetizedDishes(): LiveData<List<Dish>>

    @Query("SELECT * from dish_list ORDER BY name ASC")
    suspend fun getAlphabetizedDishesDead(): List<Dish>

    @Query("SELECT * from dish_list WHERE type = :type ORDER BY name ASC")
    suspend fun getAlphabetizedDishesByType(type: String): List<Dish>

    @Query("SELECT distinct type from dish_list ORDER BY type ASC")
    suspend fun getAlphabetizedTypes(): List<String>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(dish: Dish)

    @Delete
    suspend fun delete(dish: Dish)

    @Query("DELETE FROM dish_list WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM dish_list")
    suspend fun deleteAll()

    @Query("UPDATE dish_list SET server_id = :serverId WHERE id = :id")
    suspend fun updateServerId(serverId: Int, id: Int)

    @Query("UPDATE dish_list SET name = :name, type = :type WHERE id = :id")
    suspend fun updateDish(name: String, type: String, id: Int)
}