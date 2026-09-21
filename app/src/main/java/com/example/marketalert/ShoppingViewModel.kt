package com.example.marketalert

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ShoppingViewModel : ViewModel() {
    private val _items = MutableStateFlow<List<ShoppingItem>>(
        listOf(
            ShoppingItem("1", "Warung Makmur", false),
            ShoppingItem("2", "Toko Berkah Mart", false),
            ShoppingItem("3", "Toko Sinar Jaya", false)
        )
    )
    val items: StateFlow<List<ShoppingItem>> = _items

    fun addItem(title: String) {
        if (title.isBlank()) return
        val newItem = ShoppingItem(
            id = System.currentTimeMillis().toString(),
            title = title,
            isBought = false,
            timestamp = System.currentTimeMillis()
        )
        _items.value = _items.value + newItem
    }

    fun toggleBought(item: ShoppingItem) {
        _items.value = _items.value.map {
            if (it.id == item.id) it.copy(isBought = !it.isBought) else it
        }
    }

    fun deleteItem(item: ShoppingItem) {
        _items.value = _items.value.filter { it.id != item.id }
    }
}
