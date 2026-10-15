package com.example.marketalert

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.marketalert.ui.templates.DashboardScreen
import com.example.marketalert.ui.navigation.Screen
import com.example.marketalert.ui.navigation.NavGraph
import com.example.marketalert.ui.theme.MarketAlertTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MarketAlertTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }
}
