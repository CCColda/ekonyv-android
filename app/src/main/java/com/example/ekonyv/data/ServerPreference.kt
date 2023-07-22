package com.example.ekonyv.data

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query

@Entity(tableName = "server_preference")
data class ServerPreference(
    @PrimaryKey(autoGenerate = true)
    val id: Int,

    @ColumnInfo(name="ip")
    val ip_address: String,

    @ColumnInfo(name="last_used")
    val last_used: Long
)

@Dao
interface ServerPreferenceDao {
    @Query("SELECT * FROM server_preference")
    fun getAll(): List<ServerPreference>

    @Query("SELECT * FROM server_preference ORDER BY last_used DESC")
    fun getAllOrdered(): List<ServerPreference>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(prefs: ServerPreference)

    @Delete
    fun delete(pref: ServerPreference)
}