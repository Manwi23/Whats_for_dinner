package com.example.whats_for_dinner

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DishViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: DishRepository

    // Using LiveData and caching what allDishes returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.
    val allDishes: LiveData<List<Dish>>

    init {
        val dishDao = DishRoomDatabase.getDatabase(application, viewModelScope).dishDao()
        repository = DishRepository(dishDao)
        allDishes = repository.allDishes
//        allTypes = repository.allTypes
//        dishesOfAType = repository.getDishesByType
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(dish: Dish) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(dish)
    }

    fun delete(dish: Dish) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(dish)
    }

    suspend fun dishesOfAType(type: String): List<Dish> {
        return repository.getDishesByType(type)
    }

    suspend fun getAllTypes(): List<String> {
        return repository.getAllTypes()
    }

    suspend fun getAllDishes(): List<Dish>? {
        return repository.getAllDishes()
    }

}