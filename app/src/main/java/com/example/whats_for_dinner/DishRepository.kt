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

    suspend fun insert(dish: Dish) {
        dishDao.insert(dish)
    }

    suspend fun delete(dish: Dish) {
        dishDao.delete(dish)
    }
}