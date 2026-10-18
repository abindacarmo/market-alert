package com.example.marketalert.data

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.sql.Timestamp

@Entity(
    tableName = "shopping_items",
    foreignKeys = [
        ForeignKey (
            entity = Store::class,
            parentColumns = ["id"],
            childColumns = ["storeId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("storeId")
    ]
)

data class ShoppingItem(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val isBought: Boolean = false,
    val timestamp: Long = System.currentTimeMillis(),

    val storeId: Int,
)
