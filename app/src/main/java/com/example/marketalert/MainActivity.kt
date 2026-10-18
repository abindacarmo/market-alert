package com.example.marketalert

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.marketalert.data.MarketAlertDatabase
import com.example.marketalert.ui.navigation.NavGraph
import com.example.marketalert.ui.theme.MarketAlertTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MarketAlertTheme {
                val navController = rememberNavController()

                // 1. Create (or get) the singleton database instance
                val db = MarketAlertDatabase.getInstance(this)

                // 2. Create the ViewModel with a factory (needs the DAOs from the db)
                val viewModel:
                        ShoppingViewModel = viewModel(
                    factory = ShoppingViewModel.factory(db.storeDao(), db.shoppingItemDao())
                )

                // 3. Pass it to NavGraph
                NavGraph(navController = navController, viewModel = viewModel)
            }
        }
    }
}
