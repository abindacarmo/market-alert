package com.example.marketalert

import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ShoppingViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val collection = db.collection("shopping_items")

    private val _items = MutableStateFlow<List<ShoppingItem>>(emptyList())
    val items: StateFlow<List<ShoppingItem>> = _items

    init {
        listenToItems()
    }

    private fun listenToItems() {
        collection.orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) return@addSnapshotListener
                if (snapshot != null) {
                    val itemList = snapshot.documents.mapNotNull { doc ->
                        doc.toObject(ShoppingItem::class.java)?.copy(id = doc.id)
                    }
                    _items.value = itemList
                }
            }
    }

    fun addItem(title: String) {
        if (title.isBlank()) return
        val item = hashMapOf(
            "title" to title,
            "isBought" to false,
            "timestamp" to System.currentTimeMillis()
        )
        collection.add(item)
    }

    fun toggleBought(item: ShoppingItem) {
        collection.document(item.id).update("isBought", !item.isBought)
    }

    fun deleteItem(item: ShoppingItem) {
        collection.document(item.id).delete()
    }
}
