package com.example.marketalert.ui.templates

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.marketalert.ShoppingViewModel
import com.example.marketalert.data.Store
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DashboardScreen(
    viewModel: ShoppingViewModel,
    onNavigateToMarketList: () -> Unit = {},
    onNavigateToMarketForm: () -> Unit = {},
    onNavigateToAddShopping: () -> Unit = {},
    onNavigateToDetail: (String) -> Unit = {}
) {
    // Daftar toko dari database Room (otomatis update)
    val stores by viewModel.stores.collectAsState()
    // Format tanggal: "10 Jan 2025"
    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFA2B4EC)) // Background luar gelap sesuai gambar
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        // Kartu Utama (market-alert-card)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF6C9EC7) // Warna abu-abu muda sesuai mockup gambar
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp, horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Judul Atas
                Text(
                    text = "Market Alert",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E2538),
                        fontSize = 24.sp
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Row Dua Tombol Tambah (+)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Tombol Kiri (Gelap)
                        Card(
                            modifier = Modifier.size(width = 80.dp, height = 56.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF1E2538))
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clickable { onNavigateToMarketForm() },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "+",
                                    color = Color.White,
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        // Tombol Kanan (Terang)
                        Card(
                            modifier = Modifier.size(width = 80.dp, height = 56.dp),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F4F8))
                        ) {
                            Box(
                                modifier = Modifier
                                    .clickable{onNavigateToAddShopping()}
                                    .fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "+",
                                    color = Color(0xFF1E2538),
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    // disini ada tombol untuk melihat list toko
                    Card(
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .padding(horizontal = 100.dp)
                            .size(width = 100.dp, height = 56.dp)
                            .clickable { onNavigateToMarketList() },
                    shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF81BBF5))
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Market",
                                color = Color(0xFF1E2538),
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                }

                Spacer(modifier = Modifier.height(28.dp))

                // Judul List Shopping
                Text(
                    text = "List Shopping",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E2538),
                        fontSize = 22.sp
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // List Shopping Items dari database Room
                if (stores.isEmpty()) {
                    // Tampilan jika belum ada toko sama sekali
                    Text(
                        text = "Belum ada toko. Tekan + untuk menambah.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color(0xFF1E2538),
                            fontSize = 14.sp
                        )
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(stores, key = { it.id }) { store ->
                            ShoppingItemCard(
                                title = store.name,
                                date = "Ditulis ${dateFormat.format(Date(store.createdAt))}",
                                onClick = { onNavigateToDetail(store.name) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ShoppingItemCard(title: String, date: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .clickable { onClick() }
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF4F7FA) // Warna kartu item putih bersih kebiruan sesuai gambar
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E2538),
                    fontSize = 18.sp
                )
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = date,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color(0xFF8A94A6),
                    fontSize = 14.sp
                )
            )
        }
    }
}

