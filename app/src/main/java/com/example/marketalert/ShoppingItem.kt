package com.example.marketalert

data class ShoppingItem(
    val id: String = "",
    val title: String = "",
    val isBought: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)
