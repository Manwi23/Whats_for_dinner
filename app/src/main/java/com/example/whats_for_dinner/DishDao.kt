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

    @Query("SELECT * from dish_list where server_id = :serverId")
    suspend fun getDishByServerId(serverId: Int): List<Dish>

    @Query("SELECT * from dish_list where id = :id")
    suspend fun getDishByLocalId(id: Int): List<Dish>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(dish: Dish)

    @Delete
    suspend fun delete(dish: Dish)

    @Query("DELETE FROM dish_list WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("DELETE FROM dish_list")
    suspend fun deleteAll()

    @Query("UPDATE dish_list SET timestamp = -1 WHERE id = :id")
    suspend fun markToDelete(id: Int)

    @Query("UPDATE dish_list SET server_id = :serverId WHERE id = :id")
    suspend fun updateServerId(serverId: Int, id: Int)

    @Query("UPDATE dish_list SET name = :name, type = :type, server_id = :serverId, timestamp = :timestamp, note = :note WHERE id = :id")
    suspend fun updateDish(id: Int, name: String, type: String, serverId: Int, timestamp: Long, note: String)
}