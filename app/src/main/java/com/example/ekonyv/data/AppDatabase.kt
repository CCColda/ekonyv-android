package com.example.ekonyv.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ServerPreference::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun serverPreferenceDao(): ServerPreferenceDao

    companion object {
        fun build(context: Context) =
            Room.databaseBuilder(context, AppDatabase::class.java, "ekonyv").build()
    }
}