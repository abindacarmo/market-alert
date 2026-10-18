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
    onNavigateToDetail: (Int) -> Unit = {}
) {
    // Store list from the Room database (auto-updates)
    val stores by viewModel.stores.collectAsState()
    // Date format: "10 Jan 2025"
    val dateFormat = remember { SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFA2B4EC)) // Outer background color from the mockup
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        // Main card (market-alert-card)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(32.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF6C9EC7) // Light blue card color from the mockup
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 32.dp, horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Top title
                Text(
                    text = "Market Alert",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E2538),
                        fontSize = 24.sp
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Row of two add (+) buttons
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Left button (dark)
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

                        // Right button (light)
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

                    // Button to open the store list
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

                // Shopping list title
                Text(
                    text = "List Shopping",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E2538),
                        fontSize = 22.sp
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Shopping list items from the Room database
                if (stores.isEmpty()) {
                    // Shown when there are no stores yet
                    Text(
                        text = "No stores yet. Press + to add one.",
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
                                date = "Created ${dateFormat.format(Date(store.createdAt))}",
                                onClick = { onNavigateToDetail(store.id) }
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
            containerColor = Color(0xFFF4F7FA) // Clean bluish-white item card color from the mockup
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

