package com.example.whats_for_dinner

import android.util.Log
import androidx.lifecycle.LiveData

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class DishRepository(private val dishDao: DishDao) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allDishes: LiveData<List<Dish>> = dishDao.getAlphabetizedDishes()
//    val allTypes: List<String> = dishDao.getAlphabetizedTypes()

    suspend fun getDishesByType(type: String): List<Dish> {
        return dishDao.getAlphabetizedDishesByType(type)
    }

    // TODO: order types based on usage
    suspend fun getAllTypes(): List<String> {
        return dishDao.getAlphabetizedTypes()
    }

    suspend fun getAllDishes(): List<Dish> {
        return dishDao.getAlphabetizedDishesDead()
    }

    suspend fun getById(id: Int): Dish? {
        val list = dishDao.getDishByLocalId(id)
        if (list.isEmpty()) return null
        return list[0]
    }

    suspend fun insert(dish: Dish) {
        dishDao.insert(dish)
    }

    suspend fun delete(dish: Dish) {
        dishDao.delete(dish)
    }

    suspend fun deleteById(id: Int) {
        dishDao.deleteById(id)
    }

    suspend fun markToDelete(id: Int) {
        dishDao.markToDelete(id)
    }

    suspend fun insertOrUpdate(id: Int, dish: Dish) {
        if (id == -1) {
            dishDao.insert(dish)
            return
        }
        dishDao.updateDish(id, dish.name, dish.type, dish.serverId, dish.timestamp, dish.note)
    }

    suspend fun updateWithMask(id: Int, dish: Dish, mask: MutableMap<String, Boolean>) {
        val dishList = dishDao.getDishByLocalId(id)
        if (dishList.isEmpty()) return
        val existingDish = dishList[0]

        if ("timestamp" !in mask) {
            dish.timestamp = java.time.Instant.now().toEpochMilli()
            mask["timestamp"] = true
        }

        mask.forEach { (field, update) ->
            when(field) {
                "id" -> Unit
                "serverId" -> existingDish.serverId = if (update) dish.serverId else existingDish.serverId
                "name" -> existingDish.name = if (update) dish.name else existingDish.name
                "type" -> existingDish.type = if (update) dish.type else existingDish.type
                "timestamp" -> existingDish.timestamp = if (update) dish.timestamp else existingDish.timestamp
                "note" -> existingDish.note = if (update) dish.note else existingDish.note
            }
        }

        dishDao.updateDish(existingDish.id, existingDish.name, existingDish.type, existingDish.serverId, existingDish.timestamp, existingDish.note)
    }

    suspend fun deleteAll() {
        dishDao.deleteAll()
    }
}