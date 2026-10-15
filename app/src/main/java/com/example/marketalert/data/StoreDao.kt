package com.example.marketalert.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface StoreDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(store: Store): Long

    @Query("SELECT * FROM stores ORDER BY createdAt DESC")
    fun getAll(): Flow<List<Store>>

    @Query("SELECT * FROM stores WHERE id = :id")
    fun getById(id: Int): Flow<Store?>

    @Query("SELECT COUNT(*) FROM stores")
    suspend fun count(): Int

    @Delete
    suspend fun delete(store: Store)
}