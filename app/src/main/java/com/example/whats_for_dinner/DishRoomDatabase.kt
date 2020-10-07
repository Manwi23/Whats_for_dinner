package com.example.whats_for_dinner

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// Annotates class to be a Room Database with a table (entity) of the Dish class
@Database(entities = arrayOf(Dish::class), version = 3, exportSchema = false)
public abstract class DishRoomDatabase : RoomDatabase() {

    abstract fun dishDao(): DishDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: DishRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): DishRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DishRoomDatabase::class.java,
                    "dish_database"
                )
                    // Wipes and rebuilds instead of migrating if no Migration object.
                    // Migration is not part of this codelab.
                    .fallbackToDestructiveMigration()
                    .addCallback(DishDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }//allowMainThreadQueries można dodać przed build
        }

        private class DishDatabaseCallback(
            private val scope: CoroutineScope
        ) : RoomDatabase.Callback() {

            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                INSTANCE?.let { database ->
                    scope.launch {
                        populateDatabase(database.dishDao())
                    }
                }
            }

            suspend fun populateDatabase(dishDao: DishDao) {
                // Delete all content here.
//                dishDao.deleteAll()

                // Add sample words.
                var dish = Dish("Sample dish", "main course")
                dishDao.insert(dish)
                dish = Dish("Sample dish 2", "soup")
                dishDao.insert(dish)

            }
        }
    }

}