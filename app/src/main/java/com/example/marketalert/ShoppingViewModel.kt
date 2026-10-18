package com.example.marketalert

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.marketalert.data.ShoppingItemDao
import com.example.marketalert.data.Store
import com.example.marketalert.data.StoreDao
import kotlinx.coroutines.flow.SharingStarted
import com.example.marketalert.data.ShoppingItem
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ShoppingViewModel(
    private val storeDao: StoreDao,
    private val itemDao: ShoppingItemDao
): ViewModel() {
    val stores: StateFlow<List<Store>> = storeDao.getAll().stateIn(viewModelScope, SharingStarted.WhileSubscribed(500),emptyList())

    init {
        viewModelScope.launch {
            if (storeDao.count() == 0) {
                storeDao.insert(Store(name = "Warung Makmur", latitude = 0.0, longitude = 0.0, radius = 0f))
                storeDao.insert(Store(name = "Toko Sinar Jaya", latitude = 0.0, longitude = 0.0, radius = 0f))
                storeDao.insert(Store(name = "Indomaret Sejahtera", latitude = 0.0, longitude = 0.0, radius = 0f))
            }
        }

    }

    fun addStore(name: String, latitude: Double, longitude: Double, radius: Float) {
        if (name.isBlank()) return
        viewModelScope.launch {
            storeDao.insert(Store(name = name, latitude = latitude, longitude = longitude, radius = radius))
        }
    }

    fun deleteStore(store: Store) = viewModelScope.launch { storeDao.delete(store) }

    fun getStoreById(storeId: Int) = storeDao.getById(storeId)

    fun itemByStore(storeId: Int) = itemDao.getByStore(storeId)

    fun countRemaining(storeId: Int) = itemDao.countRemaining(storeId)

    fun addItem(name: String, storeId: Int) {
        if (name.isBlank()) return
        viewModelScope.launch {
            itemDao.insert(
                ShoppingItem(
                    name = name,
                    storeId = storeId
                )
            )
        }
    }

    fun toggleBought(item: ShoppingItem) = viewModelScope.launch { itemDao.toggleBought(item.id) }

    fun deleteItem(item: ShoppingItem) = viewModelScope.launch { itemDao.delete(item) }

    companion object {
        fun factory(storeDao: StoreDao, itemDao: ShoppingItemDao) = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ShoppingViewModel(storeDao, itemDao) as T
            }
        }
    }

}

