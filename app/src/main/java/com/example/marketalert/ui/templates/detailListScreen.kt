package com.example.marketalert.ui.templates

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.marketalert.ShoppingViewModel
import com.example.marketalert.data.ShoppingItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailItemScreen(
    viewModel: ShoppingViewModel,
    storeId: Int,
    onBackClick: () -> Unit = {}
) {
    // Load the store name from the database by ID
    val store by viewModel.getStoreById(storeId).collectAsState(initial = null)
    val storeName = store?.name ?: "Store"

    // Load shopping items for this store from the database
    val items by viewModel.itemByStore(storeId).collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = storeName,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.Black,
                                fontSize = 18.sp
                            )
                        )
                        Text(
                            text = "Shopping List #$storeId",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = Color.Gray,
                                letterSpacing = 0.5.sp
                            )
                        )
                    }
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
                actions = {
                    IconButton(onClick = { /* Options menu */ }) {
                        Icon(Icons.Default.MoreVert, contentDescription = null, tint = Color.Black)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        bottomBar = {
            // Bottom bar with 3 status buttons (black & white minimal)
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.navigationBars),
                color = Color.White,
                border = BorderStroke(1.dp, Color.Black.copy(alpha = 0.05f))
            ) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    StatusButton(
                        label = "Todo",
                        icon = Icons.AutoMirrored.Filled.List,
                        containerColor = Color.White,
                        contentColor = Color.Black,
                        modifier = Modifier.weight(1f)
                    )
                    StatusButton(
                        label = "Remaining",
                        icon = Icons.Default.Info,
                        containerColor = Color(0xFFF0F0F0),
                        contentColor = Color.Black,
                        modifier = Modifier.weight(1f)
                    )
                    StatusButton(
                        label = "Done",
                        icon = Icons.Default.CheckCircle,
                        containerColor = Color.Black,
                        contentColor = Color.White,
                        modifier = Modifier.weight(1f)
                    )
                }
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

            // Progress summary card — computed from real data
            val boughtCount = items.count { it.isBought }
            val progress = if (items.isNotEmpty()) boughtCount.toFloat() / items.size else 0f

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9)),
                border = BorderStroke(1.dp, Color.Black.copy(alpha = 0.05f))
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "List Progress",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "$boughtCount of ${items.size} items bought",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.DarkGray
                        )
                    }
                    CircularProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.size(36.dp),
                        color = Color.Black,
                        strokeWidth = 4.dp,
                        trackColor = Color.LightGray.copy(alpha = 0.3f),
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "ITEMS",
                style = MaterialTheme.typography.labelMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Black,
                    letterSpacing = 1.sp
                ),
                modifier = Modifier.padding(bottom = 12.dp)
            )

            // Shopping item list from the database
            if (items.isEmpty()) {
                // Empty state when there are no items yet
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No items yet.\nPress + below to add one.",
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = Color.Gray
                        ),
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                    itemsIndexed(items, key = { _, item -> item.id }) { index, item ->
                        ShoppingDetailRow(
                            id = (index + 1).toString().padStart(2, '0'),
                            name = item.name,
                            isBought = item.isBought,
                            onToggle = { viewModel.toggleBought(item) }  // ← checkbox is clickable!
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun StatusButton(
    label: String,
    icon: ImageVector,
    containerColor: Color,
    contentColor: Color,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = { /* UI Only */ },
        modifier = modifier.height(44.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        border = if (containerColor == Color.White) BorderStroke(1.dp, Color.Black) else null,
        contentPadding = PaddingValues(horizontal = 4.dp)
    ) {
        Icon(imageVector = icon, contentDescription = null, modifier = Modifier.size(16.dp))
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = label, fontSize = 10.sp, fontWeight = FontWeight.Bold, maxLines = 1)
    }
}

@Composable
fun ShoppingDetailRow(
    id: String,
    name: String,
    isBought: Boolean,
    onToggle: () -> Unit            // ← new callback: fired when the checkbox is tapped
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() },   // ← the whole row is tappable to toggle
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(
            width = 1.dp,
            color = if (isBought) Color.LightGray else Color.Black
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Item ID label in a small circle
            Surface(
                modifier = Modifier.size(28.dp),
                shape = CircleShape,
                color = if (isBought) Color(0xFFF0F0F0) else Color.Black,
                border = if (isBought) BorderStroke(1.dp, Color.LightGray) else null
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = id,
                        color = if (isBought) Color.Gray else Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = name,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.SemiBold,
                    color = if (isBought) Color.LightGray else Color.Black,
                    textDecoration = if (isBought) TextDecoration.LineThrough else TextDecoration.None
                )
            )

            // Minimal custom checkbox
            Surface(
                modifier = Modifier.size(22.dp),
                shape = RoundedCornerShape(4.dp),
                color = if (isBought) Color.Black else Color.Transparent,
                border = BorderStroke(1.5.dp, Color.Black)
            ) {
                if (isBought) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.padding(2.dp)
                    )
                }
            }
        }
    }
}
