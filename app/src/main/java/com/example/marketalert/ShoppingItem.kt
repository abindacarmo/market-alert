package com.example.marketalert

import com.google.firebase.firestore.DocumentId

data class ShoppingItem(
    @DocumentId val id: String = "",
    val title: String = "",
    val isBought: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)
