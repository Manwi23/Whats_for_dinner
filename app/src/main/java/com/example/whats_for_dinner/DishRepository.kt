package com.example.whats_for_dinner

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
        dishDao.updateDish(id, dish.name, dish.type, dish.serverId, dish.timestamp)
    }

    suspend fun deleteAll() {
        dishDao.deleteAll()
    }
}