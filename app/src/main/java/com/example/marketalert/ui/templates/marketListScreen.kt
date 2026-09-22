package com.example.marketalert.ui.templates

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.marketalert.ui.theme.MarketAlertTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MarketListScreen(
    onBackClick: () -> Unit = {},
    onAddStoreClick: () -> Unit = {},
    onStoreClick: (String) -> Unit = {}
) {
    // Data dummy untuk daftar toko yang sudah terdaftar
    val stores = listOf(
        MarketStore("Warung Makmur", 3),
        MarketStore("Toko Berkah Mart", 0),
        MarketStore("Toko Sinar Jaya", 5),
        MarketStore("Indomaret Sejahtera", 2),
        MarketStore("Alfamart Utama", 1)
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "My Stores",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.Black
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddStoreClick,
                containerColor = Color.Black,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add New Store")
            }
        },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "REGISTERED MARKETS",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Black,
                    letterSpacing = 1.sp
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // List Toko
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(bottom = 80.dp)
            ) {
                items(stores) { store ->
                    MarketStoreCard(
                        store = store,
                        onClick = { onStoreClick(store.name) }
                    )
                }
            }
        }
    }
}

@Composable
fun MarketStoreCard(
    store: MarketStore,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(84.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color.Black),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon Representasi Toko
            Surface(
                modifier = Modifier.size(48.dp),
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFF5F5F5),
                border = BorderStroke(1.dp, Color.Black.copy(alpha = 0.1f))
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Home, // Menggunakan Home sebagai pengganti Store
                        contentDescription = null,
                        tint = Color.Black,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = store.name,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                )
                Text(
                    text = if (store.itemCount > 0) "${store.itemCount} items left" else "All items bought",
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = if (store.itemCount > 0) Color.Black else Color.Gray,
                        fontWeight = if (store.itemCount > 0) FontWeight.Medium else FontWeight.Normal
                    )
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = Color.LightGray
            )
        }
    }
}

data class MarketStore(
    val name: String,
    val itemCount: Int
)

@Preview(showBackground = true)
@Composable
fun MarketListScreenPreview() {
    MarketAlertTheme {
        MarketListScreen()
    }
}
