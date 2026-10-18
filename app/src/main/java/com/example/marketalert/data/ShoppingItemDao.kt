package com.example.marketalert.data


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: ShoppingItem)

    @Query("SELECT * FROM shopping_items WHERE storeId = :storeId ORDER BY timestamp DESC")
    fun getByStore(storeId: Int): Flow<List<ShoppingItem>>

    @Query("UPDATE shopping_items SET isBought = NOT isBought WHERE id = :itemId")
    suspend fun toggleBought(itemId: Int)

    @Query("SELECT COUNT(*) FROM shopping_items WHERE storeId = :storeId AND isBought = 0")
    fun countRemaining(storeId: Int): Flow<Int>

    @Delete
    suspend fun delete(item: ShoppingItem)
}