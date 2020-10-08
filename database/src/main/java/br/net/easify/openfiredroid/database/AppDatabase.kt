package br.net.easify.openfiredroid.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.net.easify.openfiredroid.database.dao.*
import br.net.easify.openfiredroid.database.model.*


@Database(
    entities = [
        User::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        private var instance: AppDatabase? = null
        private var databaseName = "openfiredroid.sqlite"

        fun getAppDataBase(context: Context): AppDatabase {
            if (instance == null) {
                synchronized(AppDatabase::class) {
                    instance = Room.databaseBuilder(
                        context,
                        AppDatabase::class.java,
                        databaseName)
                        .allowMainThreadQueries()
                        .build()
                }
            }
            return instance!!
        }
    }
}