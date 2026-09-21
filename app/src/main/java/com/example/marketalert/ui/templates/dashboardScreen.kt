package com.example.marketalert.ui.templates

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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

@Composable
fun DashboardScreen() {
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
                                .clickable { /* Action */ },
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
                            modifier = Modifier.fillMaxSize(),
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

                // List Shopping Items sesuai mockup gambar
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ShoppingItemCard(title = "Warung Makmur", date = "Ditulis 10 Jan 2025")
                    ShoppingItemCard(title = "Toko Berkah Mart", date = "Ditulis 08 Jan 2025")
                    ShoppingItemCard(title = "Toko Sinar Jaya", date = "Ditulis 12 Jan 2025")
                }
            }
        }
    }
}

@Composable
fun ShoppingItemCard(title: String, date: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
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

@Preview(showBackground = true)
@Composable
fun DashboardScreenPreview() {
    MarketAlertTheme {
        DashboardScreen()
    }
}
